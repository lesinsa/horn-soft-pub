package ru.prbb.common.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;

/**
 * @author LesinSA
 */
@LogException
@Interceptor
public class TraceInterceptor {
    @AroundInvoke
    public Object aroundInvoke(InvocationContext ctx) throws Exception {
        Object result = ctx.proceed();
        Method method = ctx.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        Logger logger = LoggerFactory.getLogger(declaringClass);
        if (logger.isDebugEnabled()) {
            logger.debug("{}: {}", method.getName(), ctx.getParameters());
            logger.debug("{} - result: {}", method.getName(), result);
        }
        return result;
    }
}
