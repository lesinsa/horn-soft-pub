package com.horn.common.logging.cdi;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @author lesinsa on 15.11.2015.
 */
@Logged
@Interceptor
public class RestLogInterceptor {

    @AroundInvoke
    public Object aroundInvoke(InvocationContext ctx) throws Throwable {
        return ctx.proceed();
    }
}
