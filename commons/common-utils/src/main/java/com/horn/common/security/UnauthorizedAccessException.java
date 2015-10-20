package com.horn.common.security;

/**
 * @author lesinsa on 17.06.2015
 */
public class UnauthorizedAccessException extends UnauthorizedException {
    private final String userName;

    public UnauthorizedAccessException(String userName, String resource) {
        super(String.format("User '%s' has no acess to resource: %s", userName, resource), resource);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

}
