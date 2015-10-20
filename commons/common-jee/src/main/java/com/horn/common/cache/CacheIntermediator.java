package com.horn.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;

/**
 * Класс-посредник. Выполняет принятия решения: вызвать реальный вызов или взять значение из кэша
 *
 * @author by lesinsa on 18.09.2015.
 */
public class CacheIntermediator {
    public static final NullValue NULL_VALUE = new NullValue() {
    };
    private static final Logger LOG = LoggerFactory.getLogger(CacheIntermediator.class);
    private static final String KEY_HASH_ALG = "MD5";
    private static final byte[] NULL_BYTES = "##null##".getBytes();
    private final CacheLocator cacheLocator;

    public CacheIntermediator(CacheLocator cacheLocator) {
        this.cacheLocator = cacheLocator;
    }

    /**
     * @param target     экземпляр класса, для которого будет выполнен метод
     * @param method     метод, который будет выполнен, если не найдено значение в кэше
     * @param parameters аргументы для передачи в выполняемы метод. Все параметры рассматриваются как кэш-ключи, если
     *                   только они не помечены аннотацией @NonKey
     * @return результат выполнения или значение из кэша
     * @throws InvocationTargetException все исключения в процессе оборачиваются в InvocationTargetException.
     */
    public Object proceed(Object target, Method method, Object... parameters) throws Exception {
        return innerProceed(method, () -> {
            try {
                return method.invoke(target, parameters);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            } catch (InvocationTargetException e) {
                throw (Exception) e.getCause();
            }
        }, parameters);
    }

    public Object proceed(Method method, Callable<?> callable, Object... parameters) throws Exception {
        return innerProceed(method, callable, parameters);
    }

    private Object innerProceed(Method method, Callable<?> callable, Object... parameters) throws Exception {
        Cached annotation = method.getAnnotation(Cached.class);
        if (annotation == null) {
            return invoke(callable);
        }

        // получаем кэш по имени
        CacheStore cacheStore = cacheLocator.lookup(annotation.name());
        if (cacheStore == null) {
            LOG.warn("Cache {} is not found", annotation.name());
            return invoke(callable);
        }

        // формируем кэш-ключ
        Serializable key = createKey(method, parameters);

        // ищем значение в кэше
        Serializable result = takeResultFromCache(cacheStore, key);
        if (result != null) {
            if (!method.getReturnType().isAssignableFrom(result.getClass())) {
                // после редеплоя классы отличаются, что приводит к ClassCastException
                evictQuetly(cacheStore, key);
                result = null;
            }
            return result;
        }

        // значение не найдено - производим вызов метода
        Object retValue = invoke(callable);
        if (retValue instanceof Serializable) {
            // помещаем полученный результат в кэш
            cacheStore.put(key, (Serializable) retValue);
        } else if (retValue == null) {
            cacheStore.put(key, NULL_VALUE);
        } else {
            LOG.debug("Return value cannot be cached for method: {}", method);
        }
        return retValue;
    }

    private Object invoke(Callable<?> callable) throws Exception {
        return callable.call();
    }

    private Serializable takeResultFromCache(CacheStore cacheStore, Serializable key) {
        try {
            return cacheStore.get(key);
        } catch (RuntimeException e) {
            // убираем из кэша проблемное значение
            evictQuetly(cacheStore, key);
            throw e;
        }
    }

    private void evictQuetly(CacheStore cacheStore, Serializable key) {
        try {
            cacheStore.evict(key);
        } catch (RuntimeException e) {
            LOG.trace("", e);
        }
    }

    private Serializable createKey(Method method, Object[] args) {
        try {
            MessageDigest md5 = MessageDigest.getInstance(KEY_HASH_ALG);
            md5.update(method.getDeclaringClass().getName().getBytes());
            md5.update(method.getName().getBytes());
            Parameter[] parameters = method.getParameters();
            int i = 0;
            for (Object parameter : args) {
                NonKey nonKey = parameters[i].getAnnotation(NonKey.class);
                if (nonKey != null) {
                    continue;
                }
                byte[] bytes = parameter != null ? parameter.toString().getBytes() : NULL_BYTES;
                md5.update(bytes);
                i++;
            }
            byte[] digest = md5.digest();
            return DatatypeConverter.printHexBinary(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private interface NullValue extends Serializable {
    }
}
