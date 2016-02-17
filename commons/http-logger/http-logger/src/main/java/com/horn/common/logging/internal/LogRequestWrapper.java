package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author by lesinsa on 02.10.2015.
 */
public class LogRequestWrapper extends HttpServletRequestWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(LogRequestWrapper.class);

    private final WrapperHolder<ServletInputStream> wrapperHolder;

    public LogRequestWrapper(HttpServletRequest request, HttpLogContext logContext) {
        super(request);
        this.wrapperHolder = new InputWrapperHolder(logContext);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return wrapperHolder.getDelegate();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        LOG.warn("Request body cannot be logged because wrapping HttpServlet.getReader support is not implemented");
        return super.getReader();
    }

    public byte[] getData() {
        return wrapperHolder.getData();
    }

    private class InputWrapperHolder extends WrapperHolder<ServletInputStream> {

        public InputWrapperHolder(HttpLogContext logContext) {
            super(logContext);
        }

        @Override
        protected ServletInputStream takeUnwrapped() throws IOException {
            return LogRequestWrapper.super.getInputStream();
        }

        @Override
        protected ServletInputStream createWrapped() throws IOException {
            return new LogInputStream(LogRequestWrapper.super.getInputStream(), logContext);
        }

        @Override
        protected boolean enabled(HttpLogContext context) {
            return context.isRequestBodyEnabled();
        }
    }
}
