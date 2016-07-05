package ru.prbb.common.jpa;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author lesinsa on 17.06.2015
 */
public class WhenListener {

    protected void processEntity(Object o, Class<? extends Annotation> annotationClass) {
        Class<?> clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Annotation annotation = field.getAnnotation(annotationClass);
            if (annotation != null) {
                field.setAccessible(true);

                Object value;
                Class<?> fieldClass = field.getType();
                if (fieldClass == Timestamp.class) {
                    value = new Timestamp(System.currentTimeMillis());
                } else if (fieldClass == Date.class) {
                    value = new Date();
                } else if (fieldClass == java.sql.Date.class) {
                    value = new java.sql.Date(System.currentTimeMillis());
                } else {
                    throw new IllegalArgumentException("Unsupported date/time type: " + fieldClass.getName());
                }
                try {
                    field.set(o, value);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }
}
