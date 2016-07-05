package ru.prbb.common.ws;

import com.sun.xml.ws.developer.JAXWSProperties;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceFeature;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lesinsa
 */
public final class WebServiceBuilder {
    private static final String INNER_CONNECT_TIMEOUT = "com.sun.xml.internal.ws.connect.timeout";
    private static final String INNER_REQUEST_TIMEOUT = "com.sun.xml.internal.ws.request.timeout";

    private final Service service;
    private URL serviceURL;
    private Long timeout;
    private List<WebServiceFeature> featureList;

    public WebServiceBuilder(Service service) {
        this.service = service;
    }

    public WebServiceBuilder setServiceURL(URL serviceURL) {
        this.serviceURL = serviceURL;
        return this;
    }

    public WebServiceBuilder setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public WebServiceBuilder addFeature(WebServiceFeature feature) {
        if (featureList == null) {
            featureList = new ArrayList<WebServiceFeature>();
        }
        featureList.add(feature);
        return this;
    }

    public <T> T build(Class<T> serviceInterface) {

        T port;
        if (featureList != null && !featureList.isEmpty()) {
            Object[] featuresObj = featureList.toArray();
            WebServiceFeature[] features = Arrays.copyOf(featuresObj, featuresObj.length, WebServiceFeature[].class);
            port = service.getPort(serviceInterface, features);
        } else {
            port = service.getPort(serviceInterface);
        }
        BindingProvider bindingProvider = (BindingProvider) port;
        if (serviceURL != null) {
            bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceURL.toString());
        }
        if (timeout != null) {
            int serviceTimeout = timeout.intValue();
            bindingProvider.getRequestContext().put(JAXWSProperties.CONNECT_TIMEOUT, serviceTimeout);
            bindingProvider.getRequestContext().put(JAXWSProperties.REQUEST_TIMEOUT, serviceTimeout);
            // в среде Java SE используются другие константы из com.sun.xml.internal.ws.developer.JAXWSProperties, на который нельзя ссылаться
            bindingProvider.getRequestContext().put(INNER_CONNECT_TIMEOUT, serviceTimeout);
            bindingProvider.getRequestContext().put(INNER_REQUEST_TIMEOUT, serviceTimeout);
        }
        return port;
    }
}
