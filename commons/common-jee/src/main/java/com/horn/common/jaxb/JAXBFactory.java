package com.horn.common.jaxb;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author lesinsa on 20.05.2015.
 */
@Singleton
public class JAXBFactory {

    @Inject
    private ContextHolderBean holderBean;


    public Object deserialize(String contextName, String xml) {
        try {
            Object context = findContext(contextName);
            if (context instanceof JAXBContext) {
                JAXBContext jaxbContext = (JAXBContext) context;
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                return unmarshaller.unmarshal(new StringReader(xml));
            } else {
                throw new IllegalStateException("Not implemented yet");
            }
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    public String serialize(String contextName, Object obj) {
        try {
            Object context = findContext(contextName);
            if (context instanceof JAXBContext) {
                JAXBContext jaxbContext = (JAXBContext) context;
                Marshaller marshaller = jaxbContext.createMarshaller();
                StringWriter writer = new StringWriter();
                marshaller.marshal(obj, writer);
                return writer.toString();
            } else {
                throw new IllegalStateException("Not implemented yet");
            }

        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    public String serializeFragment(String contextName, Object obj) {
        try {
            Object context = findContext(contextName);
            if (context instanceof JAXBContext) {
                JAXBContext jaxbContext = (JAXBContext) context;
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
                StringWriter writer = new StringWriter();
                marshaller.marshal(obj, writer);
                return writer.toString();
            } else {
                throw new IllegalStateException("Not implemented yet");
            }

        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    private Object findContext(String name) {
        Object result = holderBean.get(name);
        if (result == null) {
            throw new IllegalStateException("Named serialization context is not found: " + name);
        }
        return result;
    }
}
