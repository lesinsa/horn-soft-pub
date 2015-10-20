package com.horn.common.conversion;

/**
 * @author lesinsa on 13.04.14.
 */
public class ConversionException extends Exception {
    public ConversionException(String message) {
        super(message);
    }

    public ConversionException(Exception e) {
        super(e);
    }
}
