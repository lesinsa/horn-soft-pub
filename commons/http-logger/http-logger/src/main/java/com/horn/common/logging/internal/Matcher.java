package com.horn.common.logging.internal;

/**
 * @author by lesinsa on 15.10.2015.
 */
public interface Matcher {
    Matcher ALL = value -> true;

    static Matcher get(String match) {
        return match != null && !match.equals("*") ? match::equalsIgnoreCase : ALL;
    }

    boolean matches(String value);
}
