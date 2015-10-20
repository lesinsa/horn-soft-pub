package com.horn.common.aop;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @author lesinsa
 */
@LogException
@Interceptor
public class LogExceptionInterceptor extends AbstractLogExceptionInterceptor {

    @AroundInvoke
    public Object aroundInvoke(InvocationContext ctx) throws Exception {
        return doProceed(ctx);
    }
}
