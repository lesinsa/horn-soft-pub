package ru.prbb.common.config;


import ru.prbb.common.cdi.BeanProcessorBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lesinsa on 23.03.14.
 */
@BeanProcessorBinding(provider = ConfigBeanPostProcessor.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationSettings {

    String location();

    String name() default "";

    String description() default "";


}
