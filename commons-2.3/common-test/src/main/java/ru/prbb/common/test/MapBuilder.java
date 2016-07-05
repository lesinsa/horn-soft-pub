package ru.prbb.common.test;

import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MultivaluedMap;

/**
 * @author lesinsa
 */
public class MapBuilder {
    private MultivaluedMapImpl map = new MultivaluedMapImpl();

    private MapBuilder() {
        // nothing to do
    }

    public MapBuilder add(String key, String value) {
        map.add(key, value);
        return this;
    }

    public MultivaluedMap build() {
        return map;
    }

    public static MapBuilder get() {
        return new MapBuilder();
    }
}
