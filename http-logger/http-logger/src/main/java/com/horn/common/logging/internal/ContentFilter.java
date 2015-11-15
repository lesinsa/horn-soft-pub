package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogContext;
import com.horn.common.logging.config.ContentFilterDef;

import static com.horn.common.NullUtils.coalesce;

/**
 * @author by lesinsa on 19.10.2015.
 */
public class ContentFilter {
    public static final int DEFAULT_INITIAL_REQUEST_BUFFER_SIZE = 512;
    public static final int DEFAULT_MAX_REQUEST_BUFFER_SIZE = 1024;
    public static final int DEFAULT_INITIAL_RESPONSE_BUFFER_SIZE = 1024;
    public static final int DEFAULT_MAX_RESPONSE_BUFFER_SIZE = 8192;

    public static final LogBodyConfig DEFAULT_REQUEST_BODY_CONFIG =
            new LogBodyConfig(true, ContentFilter.DEFAULT_INITIAL_REQUEST_BUFFER_SIZE,
                    ContentFilter.DEFAULT_MAX_REQUEST_BUFFER_SIZE);
    public static final LogBodyConfig DEFAULT_RESPONSE_BODY_CONFIG =
            new LogBodyConfig(true, ContentFilter.DEFAULT_INITIAL_RESPONSE_BUFFER_SIZE,
                    ContentFilter.DEFAULT_MAX_RESPONSE_BUFFER_SIZE);

    private final Matcher requestTypeMatcher;
    private final Matcher responseTypeMatcher;
    private final LogBodyConfig requestConfig;
    private final LogBodyConfig responseConfig;

    @SuppressWarnings("ConstantConditions")
    public ContentFilter(ContentFilterDef def) {
        requestTypeMatcher = MimeTypeMatcher.newInstance(def.getRequestMimeType());
        responseTypeMatcher = MimeTypeMatcher.newInstance(def.getRequestMimeType());

        int initialRequestBufferSize = coalesce(def.getInitialRequestBufferSize(), DEFAULT_INITIAL_REQUEST_BUFFER_SIZE);
        int maxRequestBufferSize = coalesce(def.getInitialRequestBufferSize(), DEFAULT_MAX_REQUEST_BUFFER_SIZE);
        requestConfig = new LogBodyConfig(!def.isDisabled() && !def.isDisableRequestBody(),
                initialRequestBufferSize, maxRequestBufferSize);

        int initialResponseBufferSize = coalesce(def.getInitialRequestBufferSize(), DEFAULT_INITIAL_RESPONSE_BUFFER_SIZE);
        int maxResponseBufferSize = coalesce(def.getInitialRequestBufferSize(), DEFAULT_MAX_RESPONSE_BUFFER_SIZE);
        responseConfig = new LogBodyConfig(!def.isDisabled() && !def.isDisableResponseBody(),
                initialResponseBufferSize, maxResponseBufferSize);
    }

    public boolean match(HttpLogContext logContext) {
        return requestTypeMatcher.matches(logContext.getRequest().getContentType()) &&
                responseTypeMatcher.matches(logContext.getResponse().getContentType());
    }

    public LogBodyConfig getRequestConfig() {
        return requestConfig;
    }

    public LogBodyConfig getResponseConfig() {
        return responseConfig;
    }
}
