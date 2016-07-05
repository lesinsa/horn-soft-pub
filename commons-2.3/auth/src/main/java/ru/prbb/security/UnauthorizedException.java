package ru.prbb.security;

import ru.prbb.common.aop.NotError;
import ru.prbb.common.exception.StandardException;

/**
 * @author MaslovDV
 */
@NotError
public class UnauthorizedException extends StandardException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
