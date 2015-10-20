package com.horn.common.xml;

import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LesinSA
 */
public abstract class IntEnumTypeAdapter<T extends Enum> extends XmlAdapter<Integer, T> {

    private static final Map<Type, EnumInfo<?>> TYPE_INFO = Collections.synchronizedMap(new HashMap<Type, EnumInfo<?>>());
    private Class<T> type;

    @SuppressWarnings("unchecked")
    public IntEnumTypeAdapter() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        type = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public T unmarshal(Integer v) throws Exception {
        if (v == null) {
            return null;
        }
        checkInit();
        return (T) TYPE_INFO.get(type).getMap1().get(v);
    }

    @Override
    public Integer marshal(T v) throws Exception {
        if (v == null) {
            return null;
        }
        checkInit();
        return TYPE_INFO.get(type).getMap2().get(v);
    }

    @SuppressWarnings("unchecked")
    private void checkInit() {
        synchronized (TYPE_INFO) {
            EnumInfo enumInfo = TYPE_INFO.get(type);
            if (enumInfo == null) {
                Map<Integer, T> map1 = new HashMap<Integer, T>();
                Map<T, Integer> map2 = new HashMap<T, Integer>();
                Field[] fields = type.getFields();
                for (Field field : fields) {
                    indexFields(map1, map2, field);
                }
                TYPE_INFO.put(type, new EnumInfo(map1, map2));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void indexFields(Map<Integer, T> map1, Map<T, Integer> map2, Field field) {
        try {
            XmlEnumValue annotation = field.getAnnotation(XmlEnumValue.class);
            if (annotation == null) {
                throw new IllegalArgumentException("Enum value is not annotated with @XmlEnumValue: " + field.getName());
            }
            Integer value = Integer.valueOf(annotation.value());
            T t = (T) field.get(null);
            map1.put(value, t);
            map2.put(t, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static class EnumInfo<T extends Enum> {
        private final Map<Integer, T> map1;
        private final Map<T, Integer> map2;

        private EnumInfo(Map<Integer, T> map1, Map<T, Integer> map2) {
            this.map1 = map1;
            this.map2 = map2;
        }

        public Map<Integer, T> getMap1() {
            return map1;
        }

        public Map<T, Integer> getMap2() {
            return map2;
        }
    }
}
