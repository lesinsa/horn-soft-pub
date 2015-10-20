package com.horn.common.config;

import java.util.Properties;

/**
 * @author lesinsa on 23.03.14.
 */
public interface ConfigLoader {

    Properties load(UrlInfo urlInfo);
}
