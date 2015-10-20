package com.horn.common.config;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author lesinsa
 */
@ConfigProtocol("classpath")
@Singleton
public class ClasspathConfigLoader implements ConfigLoader {
    @Override
    public Properties load(UrlInfo urlInfo) {
        try {
            InputStream inputStream = ClasspathConfigLoader.class.getResourceAsStream(urlInfo.getResourceName());
            if (inputStream != null) {
                Properties properties = new Properties();
                try {
                    properties.load(inputStream);
                } finally {
                    inputStream.close();
                }
                return properties;
            } else {
                throw new ConfigException("Resource not found: " + urlInfo.getResourceName());
            }
        } catch (IOException e) {
            throw new ConfigException("Error loading claaspath resource: " + urlInfo.getResourceName(), e);
        }
    }
}
