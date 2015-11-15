package com.horn.common.logging.config;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by lesinsa on 05.10.2015.
 */
@XmlType(name = "http-log-appender")
@XmlAccessorType(XmlAccessType.FIELD)
public class AppenderDef {
    @XmlID
    @XmlAttribute(name = "name", required = true)
    private String name;
    @XmlAttribute(name = "class", required = true)
    private String clazz;
    @XmlElement(name = "property")
    private List<Property> properties;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public List<Property> getProperties() {
        if (properties == null) {
            properties = new ArrayList<>();
        }
        return properties;
    }
}
