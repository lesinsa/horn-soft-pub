package ru.prbb.common.it.mocks;

import ru.prbb.common.cache.Cached;

import javax.inject.Singleton;

/**
 * @author LesinSA
 */
@Singleton
public class TestCachedClass {
    private String stringValue;
    private Object objectValue;

    @Cached(name = TestCacheConfig.TEST_CACHE_NAME)
    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    @Cached(name = TestCacheConfig.TEST_CACHE_NAME)
    public Object getObjectValue() {
        return objectValue;
    }

    public void setObjectValue(Object objectValue) {
        this.objectValue = objectValue;
    }

    @Cached(name = TestCacheConfig.TEST_CACHE_NAME)
    public String getNullValue() {
        return null;
    }
}
