package com.horn.common.config;

/**
 * @author lesinsa on 24.03.14.
 */
public class ConfigException extends RuntimeException {
    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable e) {
        super(message, e);
    }
}
