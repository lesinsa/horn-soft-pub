package com.horn.common.rest;

import com.horn.common.exception.StandardException;

/**
 * @author lesinsa
 */
public class NotFoundException extends StandardException {
    private final String exMsg;
    private final String entityClass;
    private final String id;

    public NotFoundException(Class clazz, String id, String message) {
        this(clazz.getName(), id, message);
    }

    public NotFoundException(Class clazz, String id) {
        this(clazz.getName(), id, "Сущность '" + clazz.getName() + "', id='" + id + "' не найдена");
    }

    public NotFoundException(Class clazz, Integer id, String message) {
        this(clazz.getName(), "" + id, message);
    }

    public NotFoundException(String entityClass, String id, String message) {
        super(message + ". Class = " + entityClass + ", id = " + id);
        this.entityClass = entityClass;
        this.id = id;
        exMsg = message;
    }

    public String getEntityClass() {
        return entityClass;
    }

    public String getId() {
        return id;
    }

    public String getExMsg() {
        return exMsg;
    }
}