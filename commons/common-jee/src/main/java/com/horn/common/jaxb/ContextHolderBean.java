package com.horn.common.jaxb;

import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Хранилище именнованных JAXB-контекстов используемых для сериализации и десериализации XML, и в перспективе, JSON.</p>
 * <p>Используется для управления доступом в многопоточной среде.</p>
 * Класс не thread-safe, поэтому нельзя использовать его из прикладного кода без дополнительной синхронизации.
 *
 * @author lesinsa on 20.05.2015.
 */
@Singleton
public class ContextHolderBean {

    private Map<String, Object> map;

    public ContextHolderBean() {
        map = new HashMap<>();
    }

    public JAXBContext addContext(String name, SerializerType type, Class... classes) throws JAXBException {
        if (map.containsKey(name)) {
            throw new IllegalArgumentException("Context already exists: " + name);
        }
        return updateContext(name, type, classes);
    }

    public JAXBContext updateContext(String name, SerializerType type, Class... classes) throws JAXBException {
        if (type == SerializerType.XML) {
            JAXBContext context = JAXBContext.newInstance(classes);
            map.put(name, context);
            return context;
        } else {
            throw new IllegalArgumentException("Not implemented serializer type: " + type + " for named context '" +
                    name + "'");
        }
    }

    public Object get(String name) {
        return map.get(name);
    }
}
