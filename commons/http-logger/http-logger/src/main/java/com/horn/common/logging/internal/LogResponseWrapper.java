package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogContext;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author by lesinsa on 02.10.2015.
 */
public class LogResponseWrapper extends HttpServletResponseWrapper {

    private final WrapperHolder<ServletOutputStream> wrapperHolder;
    private PrintWriter printWriter;

    public LogResponseWrapper(HttpServletResponse response, HttpLogContext logContext) {
        super(response);
        wrapperHolder = new OutputWrapperHolder(logContext);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return wrapperHolder.getDelegate();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (printWriter == null) {
            printWriter = new PrintWriterWrapper(getOutputStream(), getCharacterEncoding());
        }
        return printWriter;
    }

    public byte[] getData() {
        return wrapperHolder.getData();
    }

    private class OutputWrapperHolder extends WrapperHolder<ServletOutputStream> {
        public OutputWrapperHolder(HttpLogContext logContext) {
            super(logContext);
        }

        @Override
        protected ServletOutputStream takeUnwrapped() throws IOException {
            return LogResponseWrapper.super.getOutputStream();
        }

        @Override
        protected ServletOutputStream createWrapped() throws IOException {
            return new LogOutputStream(LogResponseWrapper.super.getOutputStream(), logContext);
        }

        @Override
        protected boolean enabled(HttpLogContext context) {
            return logContext.isResponseBodyEnabled();
        }
    }
}
