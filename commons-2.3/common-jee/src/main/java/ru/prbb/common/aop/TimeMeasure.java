package ru.prbb.common.aop;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

/**
 * User: VyalkovAK 14.08.14 14:58
 */
@Inherited
@InterceptorBinding
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeMeasure {
}
