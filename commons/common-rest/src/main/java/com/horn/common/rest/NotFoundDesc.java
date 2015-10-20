package com.horn.common.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author MaslovDV
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class NotFoundDesc {
    @XmlElement(name = "class")
    private String clazz;
    @XmlElement(name = "id")
    private String id;
    @XmlElement(name = "message")
    private String message;

    public NotFoundDesc(NotFoundException e) {
        clazz = e.getEntityClass();
        id = e.getId();
        message = e.getExMsg();
    }

    private NotFoundDesc() {
    }

    public String getClazz() {
        return clazz;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
