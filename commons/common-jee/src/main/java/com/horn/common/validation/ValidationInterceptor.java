package com.horn.common.validation;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

/**
 * @author lesinsa
 */
@Validated
@Interceptor
public class ValidationInterceptor extends MethodValidator implements Serializable {

    @AroundInvoke
    public Object validateMethodParams(InvocationContext ctx) throws Exception {
        return doValidate(ctx);
    }
}
