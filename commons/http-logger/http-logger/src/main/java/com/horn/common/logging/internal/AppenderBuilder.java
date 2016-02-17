package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogAppender;
import com.horn.common.logging.HttpLogConfigException;
import com.horn.common.logging.config.AppenderDef;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

/**
 * @author by lesinsa on 14.10.2015.
 */
public final class AppenderBuilder {

    private AppenderBuilder() {
        // nothing to do
    }

    public static HttpLogAppender build(AppenderDef def) {
        try {
            if (def.getClazz() == null) {
                throw new HttpLogConfigException("Attribute 'class' is required in appender definition");
            }

            Class<?> clazz = Class.forName(def.getClazz());
            // first, looking for construnctor with one arg of class AppenderDef
            Constructor<?> defaultConstructor = null;
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> constructor : constructors) {
                Parameter[] parameters = constructor.getParameters();
                if (parameters.length == 1 && parameters[0].getType().isAssignableFrom(AppenderDef.class)) {
                    return (HttpLogAppender) constructor.newInstance(def);
                }
                if (parameters.length == 0) {
                    defaultConstructor = constructor;
                }
            }

            // trying instantiate by default constructor
            if (defaultConstructor != null) {
                return (HttpLogAppender) defaultConstructor.newInstance();
            }

            throw new HttpLogConfigException("Cannot find appropriate constructor for appender of class '" +
                    def.getClazz() + "'");
        } catch (ClassNotFoundException e) {
            throw new HttpLogConfigException("Appender clazz '" + def.getClazz() + "' is not found", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new HttpLogConfigException("Error instantiating appender instance of class '" +
                    def.getClazz() + "'", e);
        }

    }
}
