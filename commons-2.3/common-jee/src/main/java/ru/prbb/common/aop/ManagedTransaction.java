package ru.prbb.common.aop;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

/**
 * @author LesinSA
 */
@Inherited
@InterceptorBinding
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ManagedTransaction {
}
