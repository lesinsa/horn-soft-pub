package ru.prbb.common.rest;

import ru.prbb.common.StringUtils;
import ru.prbb.common.security.AuthenticatedUser;
import ru.prbb.common.security.UnauthorizedAccessException;
import ru.prbb.common.security.UnauthorizedException;
import ru.prbb.common.security.UserContext;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author lesinsa on 17.06.2015
 */
@Secured
@Interceptor
public class RestSecurityInterceptor {
    @Inject
    private Instance<UserContext<?>> userContextInstance;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext ctx) throws Exception {
        UserContext<?> userContext = userContextInstance.get();

        // проверяем, аутентифицирован ли пользователь
        if (!userContext.isAuthenticated()) {
            throw new UnauthorizedException(takeResourceName(ctx));
        }

        Secured secured = takeAnnotation(ctx, Secured.class);
        if (secured != null) {
            // проверка прав доступа
            Class<? extends AuthenticatedUser>[] allowed = secured.roles();
            boolean authorized = false;
            for (Class<? extends AuthenticatedUser> role : allowed) {
                authorized = userContext.isUserInRole(role);
                if (authorized) {
                    break;
                }
            }
            if (!authorized) {
                // формируем исключение о несанкционированном доступе
                String resourceName = takeResourceName(ctx);
                throw new UnauthorizedAccessException(userContext.getUser().toString(), resourceName);
            }

        }
        return ctx.proceed();
    }

    private String takeResourceName(InvocationContext ctx) {
        Method method = ctx.getMethod();
        Class[] parameterTypes = method.getParameterTypes();

        List<Class> classes = Arrays.asList(parameterTypes);
        String parameterList = StringUtils.join(classes, ",");
        return String.format("%s.%s(%s)",
                method.getDeclaringClass().getName(), method.getName(), parameterList);
    }

    private <T extends Annotation> T takeAnnotation(InvocationContext ctx, Class<T> annotationClass) {
        Method method = ctx.getMethod();
        T secured = method.getAnnotation(annotationClass);
        if (secured == null) {
            secured = method.getDeclaringClass().getAnnotation(annotationClass);
        }
        return secured;
    }
}
