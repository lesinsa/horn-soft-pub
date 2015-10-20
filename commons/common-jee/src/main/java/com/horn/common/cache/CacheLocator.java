package com.horn.common.cache;

/**
 * @author by lesinsa on 19.09.2015.
 */
public interface CacheLocator {
    CacheStore lookup(String name);
}
