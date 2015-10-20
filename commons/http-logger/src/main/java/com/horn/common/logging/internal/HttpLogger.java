package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogContext;
import com.horn.common.logging.config.ContentFilterDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.horn.common.logging.HttpLogAppender;
import com.horn.common.logging.config.LoggerDef;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by lesinsa on 15.10.2015.
 */
public class HttpLogger {
    private static final Logger LOG = LoggerFactory.getLogger(HttpLogger.class);

    private final Matcher methodMatcher;
    private final Matcher requestContentTypeMatcher;
    private final boolean enableRequestBody;
    private final boolean enableResponseBody;
    private final List<ContentFilter> contentFilters;
    private List<HttpLogAppender> appenders;

    public HttpLogger(LoggerDef loggerDef) {
        this.methodMatcher = Matcher.get(loggerDef.getMethod());
        this.requestContentTypeMatcher = MimeTypeMatcher.newInstance(loggerDef.getConsumes());
        this.enableRequestBody = !loggerDef.isDisableRequestBody();
        this.enableResponseBody = !loggerDef.isDisableResponseBody();
        this.contentFilters = new ArrayList<>();
        for (ContentFilterDef filterDef : loggerDef.getContentFilters()) {
            contentFilters.add(new ContentFilter(filterDef));
        }
    }

    public boolean accept(HttpServletRequest request) {
        return methodMatcher.matches(request.getMethod()) &&
                requestContentTypeMatcher.matches(request.getContentType());
    }

    public boolean isRequestBodyLoggingEnabled(HttpLogContext context) {
        return enableRequestBody;
    }

    public boolean isResponseBodyLoggingEnabled(HttpLogContext context) {
        return enableResponseBody;
    }

    public List<HttpLogAppender> getAppenders() {
        if (appenders == null) {
            appenders = new ArrayList<>();
        }
        return appenders;
    }

    public LogBodyConfig getRequestBodyConfig(HttpLogContext logContext) {
        ContentFilter result = findContentFilter(logContext);
        return result == null ? ContentFilter.DEFAULT_REQUEST_BODY_CONFIG : result.getRequestConfig();
    }

    public LogBodyConfig getResponseBodyConfig(HttpLogContext logContext) {
        ContentFilter result = findContentFilter(logContext);
        return result == null ? ContentFilter.DEFAULT_RESPONSE_BODY_CONFIG : result.getResponseConfig();
    }

    private ContentFilter findContentFilter(HttpLogContext logContext) {
        ContentFilter result = null;
        for (ContentFilter filter : contentFilters) {
            if (filter.match(logContext)) {
                result = filter;
            }
        }
        return result;
    }

}
