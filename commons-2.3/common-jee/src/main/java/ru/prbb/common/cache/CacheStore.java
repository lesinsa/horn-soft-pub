package ru.prbb.common.cache;

import java.io.Serializable;

/**
 * @author LesinSA
 */
public interface CacheStore {

    void put(Serializable key, Serializable value);

    Serializable get(Serializable key);

    void evict(Serializable key);
}
