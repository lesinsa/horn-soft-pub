package com.horn.common.validation;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

/**
 * @author lesinsa
 */
@Inherited
@InterceptorBinding
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validated {
}
