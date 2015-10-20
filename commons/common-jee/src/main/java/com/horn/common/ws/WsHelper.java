package com.horn.common.ws;

import com.sun.xml.ws.developer.JAXWSProperties;
import org.slf4j.Logger;

import javax.net.ssl.SSLContext;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lesinsa
 *         20.02.14 10:07
 */
// TODO требуется замена
@Deprecated
public final class WsHelper {

    private static final int DEFAULT_SERVICE_TIMEOUT = 60000;
    private static final String INNER_CONNECT_TIMEOUT = "com.sun.xml.internal.ws.connect.timeout";
    private static final String INNER_REQUEST_TIMEOUT = "com.sun.xml.internal.ws.request.timeout";

    private WsHelper() {
        // nothing to do
    }

    public static <T> T initPort(Class<? extends Service> serviceClass, Class<T> serviceInterface,
                                 URL serviceAddress, int serviceTimeout) {
        Service service = getServiceInstance(serviceClass);
        return initPort(service, serviceInterface, serviceAddress, serviceTimeout);
    }

    public static <T> T initPort(Class<? extends Service> serviceClass, Class<T> serviceInterface, URL serviceAddress) {
        return initPort(serviceClass, serviceInterface, serviceAddress, DEFAULT_SERVICE_TIMEOUT);
    }

    public static <T> T initPort(Service service, Class<T> serviceInterface, URL serviceAddress, int serviceTimeout) {
        T port = service.getPort(serviceInterface);
        BindingProvider bindingProvider = (BindingProvider) port;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceAddress.toString());
        if (serviceTimeout > 0) {
            bindingProvider.getRequestContext().put(JAXWSProperties.CONNECT_TIMEOUT, serviceTimeout);
            bindingProvider.getRequestContext().put(JAXWSProperties.REQUEST_TIMEOUT, serviceTimeout);
            // в среде Java SE используются другие константы из com.sun.xml.internal.ws.developer.JAXWSProperties, на который нельзя ссылаться
            bindingProvider.getRequestContext().put(INNER_CONNECT_TIMEOUT, serviceTimeout);
            bindingProvider.getRequestContext().put(INNER_REQUEST_TIMEOUT, serviceTimeout);
        }
        return port;
    }

    public static <T> T setSSLContext(T sei, SSLContext sslContext) {
        BindingProvider bindingProvider = (BindingProvider) sei;
        bindingProvider.getRequestContext().put(JAXWSProperties.SSL_SOCKET_FACTORY, sslContext.getSocketFactory());
        return sei;
    }

    public static void addLogHandler(Service service, final Logger logger) {
        final HandlerResolver originHandlerResolver = service.getHandlerResolver();
        HandlerResolver resolver = new HandlerResolver() {
            @Override
            public List<Handler> getHandlerChain(PortInfo portInfo) {
                List<Handler> handlers = new ArrayList<Handler>();
                if (originHandlerResolver != null) {
                    handlers.addAll(originHandlerResolver.getHandlerChain(portInfo));
                }
                handlers.add(new LogMessageHandler(logger));
                return handlers;
            }
        };
        service.setHandlerResolver(resolver);
    }

    private static Service getServiceInstance(Class<? extends Service> serviceClass) {
        Service service;

        try {
            service = serviceClass.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        return service;
    }

}
