package ru.prbb.common.conversion;

import ru.prbb.common.StringUtils;
import ru.prbb.common.conversion.impl.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lesinsa on 13.04.14.
 */
public class ConverterManager {

    private final Map<String, Class> primitivesMap;
    private final Map<Class, Map<Class, Class<? extends Converter>>> registeredConverters;
    private boolean emptyStringAsNull = true;

    private ConverterManager() {
        registeredConverters = Collections.synchronizedMap(new HashMap<>());
        primitivesMap = new HashMap<String, Class>() {{
            put("int", Integer.class);
            put("long", Long.class);
            put("float", Float.class);
            put("double", Double.class);
            put("boolean", Boolean.class);
            put("char", Character.class);
        }};
    }

    public static ConverterManager getInstance(ConverterSet set) {
        if (set == ConverterSet.EMPTY) {
            return new ConverterManager();
        } else if (set == ConverterSet.STANDARD) {
            return new StandardConverterManager();
        } else {
            throw new IllegalStateException("Unknown type");
        }
    }

    public void register(Class<? extends Converter> converter) {
        Class<?>[] interfaces = converter.getInterfaces();
        for (Class<?> intf : interfaces) {
            if (intf == Converter.class) {
                Type[] actualTypeArguments =
                        ((ParameterizedType) (converter.getGenericInterfaces()[0])).getActualTypeArguments();
                putConverter((Class) actualTypeArguments[0], (Class) actualTypeArguments[1], converter);
            }
        }
    }

    public Class<? extends Converter> find(Class from, Class to) {
        return getConverter(from, to);
    }

    private void putConverter(Class sourceClass, Class targetClass, Class<? extends Converter> converterClass) {
        Map<Class, Class<? extends Converter>> classMap = registeredConverters.get(sourceClass);
        if (classMap == null) {
            classMap = new HashMap<>();
            registeredConverters.put(sourceClass, classMap);
        }
        classMap.put(targetClass, converterClass);
    }

    private Class<? extends Converter> getConverter(Class sourceClass, Class targetClass) {
        Map<Class, Class<? extends Converter>> classMap = registeredConverters.get(sourceClass);
        if (classMap == null) {
            return null;
        }
        return classMap.get(targetClass);
    }

    @SuppressWarnings("unchecked")
    public <T> T convert(Object source, Class<T> clazz) throws ConversionException {
        if (source == null) {
            return null;
        }
        Class<?> sourceClass = source.getClass();
        if (clazz.isAssignableFrom(sourceClass)) {
            return (T) source;
        }
        if (sourceClass == String.class && StringUtils.isEmpty((String) source) && emptyStringAsNull) {
            return null;
        }
        Class<T> complexClass = translateToComplexClass(clazz);
        return doConvert(source, clazz, sourceClass, complexClass);
    }

    @SuppressWarnings("unchecked")
    private <T> T doConvert(Object source, Class<T> clazz, Class<?> sourceClass, Class<T> complexClass) throws ConversionException {
        Class<? extends Converter> converterClass = getConverter(sourceClass, complexClass);
        if (converterClass == null) {
            throw new ConversionException("Converter not found for class: " + clazz.getName());
        }

        Converter converter = getConverterInstance(converterClass);
        return (T) converter.convert(source);
    }

    private Converter getConverterInstance(Class<? extends Converter> converterClass) throws ConversionException {
        try {
            return converterClass.newInstance();
        } catch (InstantiationException e) {
            throw new ConversionException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> translateToComplexClass(Class<T> clazz) {
        Class<T> complexClass;
        if (clazz.isPrimitive()) {
            // примитивные типы
            Class cl = primitivesMap.get(clazz.getName());
            complexClass = cl != null ? cl : clazz;
        } else {
            complexClass = clazz;
        }
        return complexClass;
    }

    public void setEmptyStringAsNull(boolean emptyStringAsNull) {
        this.emptyStringAsNull = emptyStringAsNull;
    }

    protected static class StandardConverterManager extends ConverterManager {

        public StandardConverterManager() {
            register(StringIntegerConverter.class);
            register(StringLongConverter.class);
            register(StringURLConverter.class);
            register(StringBigDecimalConverter.class);
            register(StringStringConverter.class);
            register(StringFloatConverter.class);
            register(StringDoubleConverter.class);
            register(StringBooleanConverter.class);
        }
    }
}
