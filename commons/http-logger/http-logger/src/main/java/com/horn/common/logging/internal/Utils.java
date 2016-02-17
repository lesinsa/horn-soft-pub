package com.horn.common.logging.internal;

import javax.servlet.http.HttpServletRequest;

/**
 * @author by lesinsa on 15.10.2015.
 */
public final class Utils {
    private Utils() {
        // nothing to do
    }

    public static String takePath(HttpServletRequest request) {
        return request.getServletPath() + (request.getPathInfo() != null ? request.getPathInfo() : "");
    }

    public static String requestToString(HttpServletRequest request) {
        return request.getMethod() + " " + takePath(request);
    }
}
