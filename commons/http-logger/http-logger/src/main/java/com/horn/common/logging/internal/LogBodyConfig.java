package com.horn.common.logging.internal;

/**
 * @author by lesinsa on 19.10.2015.
 */
public final class LogBodyConfig {
    private final boolean enabled;
    private final int initialBufferSize;
    private final int maxBufferSize;

    public LogBodyConfig(boolean enabled, int initialBufferSize, int maxBufferSize) {
        this.enabled = enabled;
        this.initialBufferSize = initialBufferSize;
        this.maxBufferSize = maxBufferSize;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getInitialBufferSize() {
        return initialBufferSize;
    }

    public int getMaxBufferSize() {
        return maxBufferSize;
    }
}
