package com.horn.common.config;

/**
 * @author lesinsa on 13.04.14.
 */
public interface LoaderResolver {
    ConfigLoader resolve(String protocol);
}
