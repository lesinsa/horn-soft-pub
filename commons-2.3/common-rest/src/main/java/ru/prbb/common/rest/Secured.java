package ru.prbb.common.rest;

import ru.prbb.common.security.AuthenticatedUser;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lesinsa on 17.06.2015
 */
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Secured {
    @Nonbinding Class<? extends AuthenticatedUser>[] roles() default {AuthenticatedUser.class};
}
