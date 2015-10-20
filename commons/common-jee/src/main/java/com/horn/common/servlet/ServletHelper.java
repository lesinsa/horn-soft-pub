package com.horn.common.servlet;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author lesinsa on 25.05.2015.
 */
public final class ServletHelper {

    public static final String ARQUILLIAN_PATH = "/ArquillianServletRunner";

    private ServletHelper() {
    }

    public static boolean isArquillianRequest(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (ARQUILLIAN_PATH.equals(((HttpServletRequest) servletRequest).getServletPath())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return true;
        }
        return false;
    }

}
