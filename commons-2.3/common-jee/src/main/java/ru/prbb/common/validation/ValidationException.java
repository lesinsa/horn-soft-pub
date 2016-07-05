package ru.prbb.common.validation;

import ru.prbb.common.exception.StandardException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>Возникает при нарушении ограничений на входные значения.</p>
 * Представляет собой обертку для unchecked-исключения ConstraintViolationException.
 *
 * @author lesinsa
 */
public class ValidationException extends StandardException {
    private final ConstraintViolationException targetException;

    public ValidationException(ConstraintViolationException e) {
        this.targetException = e;
    }

    public Set<ConstraintViolation<?>> getConstraintViolations() {
        return targetException.getConstraintViolations();
    }

    @Override
    public String getMessage() {
        String message;
        if (targetException != null) {
            Iterator<ConstraintViolation<?>> iterator = targetException.getConstraintViolations().iterator();
            if (iterator.hasNext()) {
                ConstraintViolation<?> next = iterator.next();
                message = next.getMessage();
            } else {
                message = targetException.getMessage();
            }
        } else {
            message = getMessage();
        }

        return message;
    }

    @Override
    public String getLocalizedMessage() {
        return targetException.getLocalizedMessage();
    }

    @Override
    public Throwable getCause() {
        return targetException.getCause();
    }

    @Override
    public String toString() {
        return targetException.getMessage();
    }
}
