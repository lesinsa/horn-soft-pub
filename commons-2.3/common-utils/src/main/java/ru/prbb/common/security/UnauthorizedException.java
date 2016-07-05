package ru.prbb.common.security;

/**
 * @author lesinsa on 17.06.2015
 */
public class UnauthorizedException extends RuntimeException {
    protected final String resource;

    public UnauthorizedException(String message, String resource) {
        super(message);
        this.resource = resource;
    }

    public UnauthorizedException(String resource) {
        super("Unauthorized access to: " + resource);
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
