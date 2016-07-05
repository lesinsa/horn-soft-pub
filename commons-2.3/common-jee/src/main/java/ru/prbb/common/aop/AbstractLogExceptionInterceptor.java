package ru.prbb.common.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author lesinsa
 *         Date: 28.02.2015.
 *         Time: 14:28
 */
public class AbstractLogExceptionInterceptor implements Serializable {
    protected Object doProceed(InvocationContext ctx) throws Exception {
        try {
            return ctx.proceed();
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            StringBuilder sb = new StringBuilder("ConstraintViolationException is occurred. Violations are^\n");
            for (ConstraintViolation<?> violation : violations) {
                sb.append(violation);
            }
            Logger logger = getLogger(ctx);
            logger.error(sb.toString());
            throw e;
        } catch (Exception e) {
            Logger logger = getLogger(ctx);
            NotError notError = e.getClass().getAnnotation(NotError.class);
            if (notError == null) {
                logger.error("Invocation error", e);
            } else {
                logger.debug("", e);
            }
            throw e;
        }
    }

    private Logger getLogger(InvocationContext ctx) {
        Method method = ctx.getMethod();
        return method != null ? LoggerFactory.getLogger(method.getDeclaringClass())
                : LoggerFactory.getLogger(LogExceptionInterceptor.class);
    }

}
