package ru.prbb.common.biz;

/**
 * @author lesinsa on 09.04.2015.
 */
public final class Param {
    private final String name;
    private final String value;

    public Param(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
