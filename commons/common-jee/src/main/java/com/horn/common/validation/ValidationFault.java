package com.horn.common.validation;

import com.horn.common.exception.StandardException;
import com.horn.common.lang.SerializableList;

import javax.validation.ConstraintViolation;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.WebFault;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс для возврата сообщения об ошибке из SOAP-сервисов. Возвращает клиенту простое текстовое сообщение с описанием ошибки.
 *
 * @author lesinsa
 */
@WebFault(targetNamespace = "http://common.prbb.ru/schema")
public class ValidationFault extends StandardException {
    private ValidationFaultBean faultInfo;

    @SuppressWarnings("UnusedDeclaration")
    public ValidationFault(String message, ValidationFaultBean faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    @SuppressWarnings("UnusedDeclaration")
    public ValidationFault(String message, String validationFaultMessage, SerializableList<Violation> details) {
        super(message);
        this.faultInfo = new ValidationFaultBean(validationFaultMessage, details);
    }

    @SuppressWarnings("UnusedDeclaration")
    public ValidationFault(String message, ValidationFaultBean faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    @SuppressWarnings("UnusedDeclaration")
    public ValidationFault(String message, Throwable cause) {
        this(message, new ValidationFaultBean(), cause);
    }

    public ValidationFault(ValidationException e) {
        // TODO текст сообщения
        super("Ошибка валидации");
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        SerializableList<Violation> violations =
                (SerializableList<Violation>) new ArrayList<Violation>(constraintViolations.size());
        violations.addAll(constraintViolations.stream().map(Violation::new).collect(Collectors.toList()));
        this.faultInfo = new ValidationFaultBean("Ошибка валидации", violations);
    }


    public ValidationFaultBean getFaultInfo() {
        return faultInfo;
    }

    @XmlType
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ValidationFaultBean implements Serializable {
        private String message;
        private SerializableList<Violation> details;

        public ValidationFaultBean() {
        }

        public ValidationFaultBean(String message, SerializableList<Violation> details) {
            this.message = message;
            this.details = details;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Violation> getDetails() {
            return details;
        }

        public void setDetails(SerializableList<Violation> details) {
            this.details = details;
        }
    }
}
