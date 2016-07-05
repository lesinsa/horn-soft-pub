package ru.prbb.common.config;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

/**
 * @author lesinsa on 13.04.14.
 */
public class UrlInfo {

    private final String protocol;
    private final String resourceName;
    private final Properties properties;

    public UrlInfo(String location) {
        properties = new Properties();

        int protocolSepIndex = location.indexOf(":");
        if (protocolSepIndex < 0 && protocolSepIndex < location.length()) {
            throw new ConfigException("Config protocol is not found for location: " + location);
        }
        protocol = location.substring(0, protocolSepIndex);

        int nsSepIndex = location.indexOf("?");
        String resourceLocalName;
        if (nsSepIndex < 0) {
            resourceLocalName = location.substring(protocolSepIndex + 1, location.length());
        } else {
            resourceLocalName = location.substring(protocolSepIndex + 1, nsSepIndex);

            String params = location.substring(nsSepIndex + 1);
            String replaced = params.replace("&", "\n");
            try {
                properties.load(new StringReader(replaced));
            } catch (IOException e) {
                throw new ConfigException("Error reading properties from resource url", e);
            }
        }
        resourceName = resourceLocalName.startsWith("//") ? resourceLocalName.substring(2) : resourceLocalName;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getResourceName() {
        return resourceName;
    }

    public Properties getProperties() {
        return properties;
    }

    public String getProperty(String name) {
        return properties.getProperty(name);
    }

}
