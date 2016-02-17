package com.horn.common.logging;

/**
 * @author by lesinsa on 14.10.2015.
 */
public class HttpLogConfigException extends RuntimeException {
    public HttpLogConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpLogConfigException(String message) {
        super(message);
    }
}
