package ru.prbb.common.validation;

import org.hibernate.validator.method.MethodConstraintViolation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author LesinSA
 */
public class MethodValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodValidator.class);
    @Inject
    @Factory
    private ValidatorFactory validatorFactory;

    protected Object doValidate(InvocationContext ctx) throws Exception {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(Arrays.toString(ctx.getParameters()));
        }
        Method method = ctx.getMethod();
        Set<ConstraintViolation<?>> violationSet = takeViolations(ctx, method);
        if (violationSet.isEmpty()) {
            // все ОК
            return ctx.proceed();
        } else {
            // при проверке выявлены нарушения
            ConstraintViolationException e = new ConstraintViolationException(violationSet);
            Class<?>[] exceptionTypes = method.getExceptionTypes();
            for (Class<?> exceptionType : exceptionTypes) {
                if (exceptionType.isAssignableFrom(ValidationException.class)) {
                    throw new ValidationException(e);
                }
            }
            // в сигнатуре метода отсутствует ValidationException, поэтому заворачиваем в runtime-исключение
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("deprecation")
    private Set<ConstraintViolation<?>> takeViolations(InvocationContext ctx, Method method) throws InstantiationException, IllegalAccessException {

        Validator validator = validatorFactory.getValidator();
        org.hibernate.validator.method.MethodValidator methodValidator =
                validator.unwrap(org.hibernate.validator.method.MethodValidator.class);

        Class<?>[] validationGroups = takeValidationGroups(method);

        Set<MethodConstraintViolation<Object>> set = methodValidator.validateAllParameters(method.getDeclaringClass()
                .newInstance(), method, ctx.getParameters(), validationGroups);

        return set.stream().collect(Collectors.toSet());
    }

    private Class<?>[] takeValidationGroups(Method method) {
        RulesOf methodRulesOf = method.getAnnotation(RulesOf.class);
        RulesOf classRulesOf = method.getDeclaringClass().getAnnotation(RulesOf.class);

        if (methodRulesOf == null && classRulesOf == null) {
            // небольшая оптимизация
            return new Class[0];
        }

        Class[] methodGroups = methodRulesOf != null ? methodRulesOf.value() : new Class[0];
        Class[] classsGroups = classRulesOf != null ? classRulesOf.value() : new Class[0];

        Class[] groups = new Class[methodGroups.length + classsGroups.length];

        // приоритет: сначала группы уровня метода, а потом класса
        System.arraycopy(methodGroups, 0, groups, 0, methodGroups.length);
        System.arraycopy(classsGroups, 0, groups, methodGroups.length, classsGroups.length);

        return groups;
    }
}
