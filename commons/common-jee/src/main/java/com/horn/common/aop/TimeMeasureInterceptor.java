package com.horn.common.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @author LesinSA
 */
@TimeMeasure
@Interceptor
public class TimeMeasureInterceptor {
    @AroundInvoke
    public Object aroundInvoke(InvocationContext ctx) throws Exception {
        Class<?> declaringClass = ctx.getMethod().getDeclaringClass();
        Logger logger = LoggerFactory.getLogger(declaringClass);
        long t = System.currentTimeMillis();
        try {
            return ctx.proceed();
        } finally {
            long time = System.currentTimeMillis() - t;
            logger.debug("Time measure: {}.{}, time = {}", declaringClass.getName(), ctx.getMethod().getName(), time);
        }
    }
}
