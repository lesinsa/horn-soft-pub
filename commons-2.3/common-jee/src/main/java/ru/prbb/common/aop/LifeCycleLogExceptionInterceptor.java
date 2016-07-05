package ru.prbb.common.aop;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @author lesinsa
 */
@LifeCycleLogException
@Interceptor
public class LifeCycleLogExceptionInterceptor extends AbstractLogExceptionInterceptor {

    @AroundInvoke
    public Object aroundInvoke(InvocationContext ctx) throws Exception {
        return doProceed(ctx);
    }

    @PostConstruct
    public void postConstruct(InvocationContext ctx) throws Exception {
        doProceed(ctx);
    }

    @PreDestroy
    public void preDestroy(InvocationContext ctx) throws Exception {
        doProceed(ctx);
    }

}
