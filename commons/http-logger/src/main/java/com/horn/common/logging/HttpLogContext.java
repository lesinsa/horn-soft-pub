package com.horn.common.logging;

import com.horn.common.logging.internal.LogBodyConfig;
import com.horn.common.logging.internal.LogRequestWrapper;
import com.horn.common.logging.internal.LogResponseWrapper;
import com.horn.common.logging.internal.HttpLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

/**
 * @author by lesinsa on 26.09.2015.
 */
public class HttpLogContext implements AutoCloseable {
    // immutable data
    private final HttpLogger httpLogger;
    private final LogRequestWrapper request;
    private final LogResponseWrapper response;
    private final String id;
    private final long startTime;
    // mutable data
    private Exception exception;

    HttpLogContext(HttpServletRequest request, HttpServletResponse response, HttpLogger httpLogger) {
        this.httpLogger = httpLogger;
        this.request = new LogRequestWrapper(request, this);
        this.response = new LogResponseWrapper(response, this);
        this.startTime = System.currentTimeMillis();
        id = UUID.randomUUID().toString();
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public String getId() {
        return id;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public long getStartTime() {
        return startTime;
    }

    public byte[] getRequestBody() {
        return request.getData();
    }

    public byte[] getResponseBody() {
        return response.getData();
    }

    @Override
    public void close() throws Exception {
        HttpLogManager.removeThreadRef();
    }

    public void save() {
        List<HttpLogAppender> appenders = httpLogger.getAppenders();
        for (HttpLogAppender appender : appenders) {
            appender.append(this);
        }
    }

    public boolean isRequestBodyEnabled() {
        return httpLogger.isRequestBodyLoggingEnabled(this);
    }

    public boolean isResponseBodyEnabled() {
        return httpLogger.isRequestBodyLoggingEnabled(this);
    }

    public LogBodyConfig getRequestBodyConfig() {
        return httpLogger.getRequestBodyConfig(this);
    }

    public LogBodyConfig getResponseBodyConfig() {
        return httpLogger.getResponseBodyConfig(this);
    }
}
