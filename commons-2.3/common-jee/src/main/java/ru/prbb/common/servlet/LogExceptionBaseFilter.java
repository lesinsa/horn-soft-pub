package ru.prbb.common.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author MaslovDV
 */
public class LogExceptionBaseFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(LogExceptionBaseFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (ServletException | RuntimeException e) {
            LOG.error("", e);
            throw e;
        } catch (IOException e) {
            LOG.debug("", e);
            throw e;
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // nothing to do
    }

    @Override
    public void destroy() {
        // nothing to do
    }
}
