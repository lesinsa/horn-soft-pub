package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogContext;

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author by lesinsa on 02.10.2015.
 */
public class LogOutputStream extends ServletOutputStream implements DataProvider {
    private final ServletOutputStream delegate;
    private final HttpLogContext logContext;
    private boolean started;
    private ByteArrayOutputStream out;
    private int maxSize;
    private int size;

    LogOutputStream(ServletOutputStream outputStream, HttpLogContext logContext) {
        this.delegate = outputStream;
        this.logContext = logContext;
    }

    @Override
    public void write(int b) throws IOException {
        if (!started) {
            LogBodyConfig config = logContext.getResponseBodyConfig();
            if (config.isEnabled()) {
                out = new ByteArrayOutputStream(config.getInitialBufferSize());
                maxSize = config.getMaxBufferSize();
            }
            started = true;
        }
        if (out != null && size < maxSize) {
            out.write(b);
            size++;
        }
        delegate.write(b);
    }

    @Override
    public byte[] getData() {
        return out != null ? out.toByteArray() : null;
    }
}
