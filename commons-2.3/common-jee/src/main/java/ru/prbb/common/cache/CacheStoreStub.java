package ru.prbb.common.cache;

import java.io.Serializable;

/**
 * Заглушка для интерфейса CacheStore, которая ничего не делает.
 *
 * @author by lesinsa on 17.09.2015.
 */
public class CacheStoreStub implements CacheStore {
    private static CacheStoreStub instance;

    public static CacheStoreStub getInstance() {
        if (instance == null) {
            instance = new CacheStoreStub();
        }
        return instance;
    }

    @Override
    public void put(Serializable key, Serializable value) {
        // stub
    }

    @Override
    public Serializable get(Serializable key) {
        return null;
    }

    @Override
    public void evict(Serializable key) {
        // stub
    }
}
