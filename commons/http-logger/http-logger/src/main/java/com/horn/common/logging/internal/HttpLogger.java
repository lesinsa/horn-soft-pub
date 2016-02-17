package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogAppender;
import com.horn.common.logging.HttpLogContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * @author lesinsa on 16.11.2015.
 */
public abstract class HttpLogger {
    protected final List<HttpLogAppender> appenders;

    public HttpLogger(List<HttpLogAppender> appenders) {
        this.appenders = Collections.unmodifiableList(appenders);
    }

    public abstract boolean accept(HttpServletRequest request);

    public abstract boolean isRequestBodyLoggingEnabled(HttpLogContext context);

    public abstract boolean isResponseBodyLoggingEnabled(HttpLogContext context);

    public List<HttpLogAppender> getAppenders() {
        return appenders;
    }

    public abstract LogBodyConfig getRequestBodyConfig(HttpLogContext logContext);

    public abstract LogBodyConfig getResponseBodyConfig(HttpLogContext logContext);
}
