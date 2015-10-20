package com.horn.common.aop;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

/**
 * @author lesinsa
 */
@Inherited
@InterceptorBinding
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LifeCycleLogException {
}
