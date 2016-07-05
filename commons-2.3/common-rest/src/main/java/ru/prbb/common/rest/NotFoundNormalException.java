package ru.prbb.common.rest;

import ru.prbb.common.aop.NotError;

/**
 * @author LesinSA
 */
@NotError
public class NotFoundNormalException extends NotFoundException {
    public NotFoundNormalException(Class clazz, String id, String message) {
        super(clazz, id, message);
    }
}
