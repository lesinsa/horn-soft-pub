package com.horn.common.aop;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @author LesinSA
 */
@ManagedTransaction
@Interceptor
public class RollbackTransactionInterceptor {

    @Resource
    private SessionContext sessionContext;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext ctx) throws Exception {
        try {
            Object proceed = ctx.proceed();
            sessionContext.setRollbackOnly();
            return proceed;
        } catch (Exception e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

}
