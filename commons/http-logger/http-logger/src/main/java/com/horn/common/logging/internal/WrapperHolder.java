package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogContext;

import java.io.IOException;

/**
 * @author by lesinsa on 17.10.2015.
 */
public abstract class WrapperHolder<T> implements DataProvider {

    protected final HttpLogContext logContext;
    private WrapMode mode;
    private T delegate;

    public WrapperHolder(HttpLogContext logContext) {
        this.logContext = logContext;
    }

    protected abstract T takeUnwrapped() throws IOException;

    protected abstract T createWrapped() throws IOException;

    protected abstract boolean enabled(HttpLogContext context);

    public T getDelegate() throws IOException {
        if (mode == null) {
            boolean enabled = enabled(logContext);
            if (enabled) {
                mode = WrapMode.WRAPPED;
                delegate = createWrapped();
                return delegate;
            } else {
                mode = WrapMode.UNWRAPPED;
                return takeUnwrapped();
            }
        }
        switch (mode) {
            case WRAPPED:
                return delegate;
            case UNWRAPPED:
                return takeUnwrapped();
            default:
                throw new IllegalStateException("Unknown wrapping mode");
        }
    }

    @Override
    public byte[] getData() {
        return mode == WrapMode.WRAPPED ? ((DataProvider) delegate).getData() : null;
    }
}
