package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogContext;

import javax.servlet.ServletInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author by lesinsa on 02.10.2015.
 */
public class LogInputStream extends ServletInputStream implements DataProvider {

    private final ServletInputStream delegate;
    private final HttpLogContext logContext;
    private boolean started;
    private ByteArrayOutputStream out;
    private int size;
    private int maxSize;

    LogInputStream(ServletInputStream inputStream, HttpLogContext logContext) {
        this.delegate = inputStream;
        this.logContext = logContext;
    }

    @Override
    public int read() throws IOException {
        int r = delegate.read();
        if (!started) {
            LogBodyConfig config = logContext.getRequestBodyConfig();
            if (config.isEnabled()) {
                out = new ByteArrayOutputStream(config.getInitialBufferSize());
                maxSize = config.getMaxBufferSize();
            }
            started = true;
        }
        if (out != null && size < maxSize && r != -1) {
            out.write(r);
            size++;
        }
        return r;
    }

    @Override
    public byte[] getData() {
        return out != null ? out.toByteArray() : null;
    }
}
