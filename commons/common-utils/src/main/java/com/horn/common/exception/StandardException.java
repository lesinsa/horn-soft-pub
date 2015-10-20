package com.horn.common.exception;

/**
 * @author lesinsa
 */
@SuppressWarnings("UnusedDeclaration")
public class StandardException extends Exception {
    public StandardException() {
    }

    public StandardException(String message) {
        super(message);
    }

    public StandardException(String message, Throwable cause) {
        super(message, cause);
    }

    public StandardException(Throwable cause) {
        super(cause);
    }
}
