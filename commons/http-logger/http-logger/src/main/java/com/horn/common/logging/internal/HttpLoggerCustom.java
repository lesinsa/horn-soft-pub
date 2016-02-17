package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogAppender;
import com.horn.common.logging.HttpLogContext;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lesinsa on 16.11.2015.
 */
class HttpLoggerCustom extends HttpLogger {

    private final HttpLogConfigurator delegate;

    HttpLoggerCustom(List<HttpLogAppender> appenders, HttpLogConfigurator delegate) {
        super(appenders);
        this.delegate = delegate;
    }

    @Override
    public boolean accept(HttpServletRequest request) {
        return true;
    }

    @Override
    public boolean isRequestBodyLoggingEnabled(HttpLogContext context) {
        return delegate.isRequestBodyLoggingEnabled(context);
    }

    @Override
    public boolean isResponseBodyLoggingEnabled(HttpLogContext context) {
        return delegate.isResponseBodyLoggingEnabled(context);
    }

    @Override
    public LogBodyConfig getRequestBodyConfig(HttpLogContext logContext) {
        return delegate.getRequestBodyConfig(logContext);
    }

    @Override
    public LogBodyConfig getResponseBodyConfig(HttpLogContext logContext) {
        return delegate.getResponseBodyConfig(logContext);
    }
}
