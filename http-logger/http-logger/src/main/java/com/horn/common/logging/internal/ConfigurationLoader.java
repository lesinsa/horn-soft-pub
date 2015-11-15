package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogConfigException;
import com.horn.common.logging.config.HttpLogConfiguration;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author by lesinsa on 15.10.2015.
 */
public final class ConfigurationLoader {
    private ConfigurationLoader() {
        // nothing to do
    }

    public static HttpLogConfiguration load(InputStream inputStream) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setNamespaceAware(true);
            // TODO schema validation
            builderFactory.setValidating(false);
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            String namespaceURI = document.getFirstChild().getNamespaceURI();

            if (!"http://xmlns.prbb.ru/schema/http-logger-1.0.xsd".equals(namespaceURI)) {
                throw new HttpLogConfigException("Configuration file is corrupted. Root element has unknown NS=" +
                        namespaceURI);
            }

            JAXBContext context = JAXBContext.newInstance(HttpLogConfiguration.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (HttpLogConfiguration) unmarshaller.unmarshal(document);
        } catch (JAXBException | ParserConfigurationException | SAXException | IOException e) {
            throw new HttpLogConfigException("Error loading configuration", e);
        }
    }
}
