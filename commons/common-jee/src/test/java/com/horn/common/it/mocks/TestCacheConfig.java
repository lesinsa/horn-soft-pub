package com.horn.common.it.mocks;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import com.horn.common.cache.CacheStore;

import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.Serializable;

/**
 * @author lesinsa
 */
public class TestCacheConfig {

    public static final String TEST_CACHE_NAME = "testCache";

    @Named(TEST_CACHE_NAME)
    @Produces
    @Singleton
    public CacheStore getDefault() {
        CacheManager cacheManager = CacheManager.create();
        Cache cache = cacheManager.getCache(TEST_CACHE_NAME);
        if (cache == null) {
            CacheConfiguration configuration = new CacheConfiguration(TEST_CACHE_NAME, 100);
            cache = new Cache(configuration);
            cacheManager.addCache(cache);
        }
        return new CacheStoreImpl(cache);
    }


    public static class CacheStoreImpl implements CacheStore {
        private final Cache cache;

        public CacheStoreImpl(Cache cache) {
            this.cache = cache;
        }


        @Override
        public void put(Serializable key, Serializable value) {
            cache.put(new Element(key, value));
        }

        @Override
        public Serializable get(Serializable key) {
            Element element = cache.get(key);
            return element != null ? (Serializable) element.getObjectValue() : null;
        }

        @Override
        public void evict(Serializable key) {
            cache.remove(key);
        }
    }
}
