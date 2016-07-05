package ru.prbb.common.validation;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

/**
 * @author lesinsa
 */
public class MethodValidationInterceptor extends MethodValidator implements Serializable {

    @AroundInvoke
    public Object validateMethodParams(InvocationContext ctx) throws Exception {
        return doValidate(ctx);
    }
}
