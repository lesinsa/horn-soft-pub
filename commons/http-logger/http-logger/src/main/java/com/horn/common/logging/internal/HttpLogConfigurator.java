package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogConfigException;
import com.horn.common.logging.HttpLogContext;

/**
 * @author lesinsa on 15.11.2015.
 */
public interface HttpLogConfigurator {
    static HttpLogConfigurator getInstance(Class<? extends HttpLogConfigurator> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    static HttpLogConfigurator getInstance(String className) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class aClass = classLoader.loadClass(className);
            if (!HttpLogConfigurator.class.isAssignableFrom(aClass)) {
                throw new HttpLogConfigException("Class " + aClass.getName() +
                        " is not subclass of HttpLogConfigurator");
            }
            return getInstance(aClass);
        } catch (ClassNotFoundException e) {
            throw new HttpLogConfigException("Custom configurator class is not found", e);
        }
    }

    boolean isRequestBodyLoggingEnabled(HttpLogContext context);

    boolean isResponseBodyLoggingEnabled(HttpLogContext context);

    LogBodyConfig getRequestBodyConfig(HttpLogContext logContext);

    LogBodyConfig getResponseBodyConfig(HttpLogContext logContext);

}
