package ru.prbb.common.aop;

import java.lang.annotation.*;

/**
 * @author lesinsa
 */
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotError {
}
