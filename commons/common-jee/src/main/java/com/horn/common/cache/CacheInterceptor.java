package com.horn.common.cache;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @author LesinSA
 */
@Cached
@Interceptor
public class CacheInterceptor {

    @Inject
    private CDICacheLocator cacheLocator;
    private CacheIntermediator intermediator;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext ctx) throws Exception {
        return getIntermediator().proceed(ctx.getMethod(), ctx::proceed, ctx.getParameters());
    }

    private CacheIntermediator getIntermediator() {
        if (intermediator == null) {
            intermediator = new CacheIntermediator(cacheLocator);
        }
        return intermediator;
    }


}
