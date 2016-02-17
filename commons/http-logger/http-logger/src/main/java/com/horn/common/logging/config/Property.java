package com.horn.common.logging.config;

import javax.xml.bind.annotation.*;

/**
 * @author by lesinsa on 14.10.2015.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Property {
    @XmlAttribute
    private String name;
    @XmlValue
    private String value;

    public Property() {
        // required by JAXB
    }

    public Property(String name, String value) {
        this();
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
