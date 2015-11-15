package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogAppender;
import com.horn.common.logging.HttpLogContext;
import com.horn.common.logging.config.ContentFilterDef;
import com.horn.common.logging.config.LoggerDef;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by lesinsa on 15.10.2015.
 */
// TODO use HttpLogConfigurator in HttpContext instead of HttpLogger
class HttpLoggerImpl extends HttpLogger implements HttpLogConfigurator {
    private final Matcher methodMatcher;
    private final Matcher requestContentTypeMatcher;
    private final boolean enableRequestBody;
    private final boolean enableResponseBody;
    private final List<ContentFilter> contentFilters;

    public HttpLoggerImpl(LoggerDef loggerDef, List<HttpLogAppender> appenders) {
        super(appenders);
        this.methodMatcher = Matcher.get(loggerDef.getMethod());
        this.requestContentTypeMatcher = MimeTypeMatcher.newInstance(loggerDef.getConsumes());
        this.enableRequestBody = !loggerDef.isDisableRequestBody();
        this.enableResponseBody = !loggerDef.isDisableResponseBody();
        this.contentFilters = new ArrayList<>();
        for (ContentFilterDef filterDef : loggerDef.getContentFilters()) {
            if (filterDef.getRequestMimeType() != null) {
                contentFilters.add(new ContentFilter(filterDef));
            }
        }
    }

    @Override
    public boolean accept(HttpServletRequest request) {
        return methodMatcher.matches(request.getMethod()) &&
                requestContentTypeMatcher.matches(request.getContentType());
    }

    @Override
    public boolean isRequestBodyLoggingEnabled(HttpLogContext context) {
        return enableRequestBody;
    }

    @Override
    public boolean isResponseBodyLoggingEnabled(HttpLogContext context) {
        return enableResponseBody;
    }

    @Override
    public LogBodyConfig getRequestBodyConfig(HttpLogContext logContext) {
        ContentFilter result = findContentFilter(logContext);
        return result == null ? ContentFilter.DEFAULT_REQUEST_BODY_CONFIG : result.getRequestConfig();
    }

    @Override
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
