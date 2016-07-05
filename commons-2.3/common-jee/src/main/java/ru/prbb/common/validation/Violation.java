package ru.prbb.common.validation;

import javax.validation.ConstraintViolation;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * @author lesinsa
 */
@XmlType
public class Violation implements Serializable {

    /**
     * Путь к элементу с невалидным значением
     */
    private String propertyPath;
    /**
     * Класс ошибки
     */
    private String clazz;
    /**
     * Текстовое сообщение, описывающее нарушение требований к значению
     */
    private String message;

    public Violation() {
    }

    public Violation(ConstraintViolation<?> e) {
        this();
        propertyPath = e.getPropertyPath().toString();
        clazz = e.getConstraintDescriptor().getAnnotation().annotationType().getName();
        message = e.getMessage();
    }

    public Violation(String propertyPath, String clazz, String message) {
        this.propertyPath = propertyPath;
        this.clazz = clazz;
        this.message = message;
    }

    @XmlElement(name = "path")
    public String getPropertyPath() {
        return propertyPath;
    }

    public void setPropertyPath(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    @XmlElement(name = "class")
    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
