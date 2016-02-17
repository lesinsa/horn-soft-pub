package com.horn.common.logging;

import com.horn.common.logging.config.HttpLogConfiguration;
import com.horn.common.logging.internal.ConfigurationLoader;
import com.horn.common.logging.internal.Utils;
import org.slf4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author by lesinsa on 21.09.2015.
 */
public class HttpLogFilter implements Filter {
    public static final String DEFAULT_CONFIG_XML = "http-log-config.xml";
    public static final String CONFIG_PATH_PARAM = "logger.path";
    public static final String HTTP_LOG_CONTEXT_ATTR = "http.log.context";
    public static final String HTTP_LOGGER_MANAGER_BASE = "http.logger.manager.";
    private static final Logger LOG = getLogger(HttpLogFilter.class);
    private String attributeName;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            LOG.trace("Handling HTTP request");
            HttpLogManager logManager = (HttpLogManager) servletRequest.getServletContext().getAttribute(attributeName);
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpLogContext logContext = logManager.handle(request, (HttpServletResponse) servletResponse);
            if (logContext != null) {
                request.setAttribute(HTTP_LOG_CONTEXT_ATTR, logContext);
                try {
                    filterChain.doFilter(logContext.getRequest(), logContext.getResponse());
                } catch (Exception e) {
                    logContext.setException(e);
                    throw e;
                }
                logContext.save();
            } else {
                if (LOG.isDebugEnabled()) {
                    String path = Utils.takePath(request);
                    LOG.debug("Not loggable request: {}", path);
                }
                filterChain.doFilter(servletRequest, servletResponse);
            }

        } else {
            LOG.trace("Passing by non-HTTP request");
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // defining unique name of ServletContext parameter
        attributeName = HTTP_LOGGER_MANAGER_BASE + filterConfig.getFilterName();

        // creating HttpLogManager
        String path = filterConfig.getInitParameter(CONFIG_PATH_PARAM);
        path = path != null ? path : DEFAULT_CONFIG_XML;
        try {
            try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
                if (inputStream == null) {
                    throw new FileNotFoundException(path);
                }
                HttpLogConfiguration configuration = ConfigurationLoader.load(inputStream);
                HttpLogManager logManager = HttpLogManager.getInstance(configuration);
                filterConfig.getServletContext().setAttribute(attributeName, logManager);
            }
        } catch (IOException e) {
            throw new ServletException("Error loading XML configuration in servlet filter '" +
                    filterConfig.getFilterName() + "': " + path, e);
        }

    }

    @Override
    public void destroy() {
        // nothing to do
    }
}
