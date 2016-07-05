package ru.prbb.common.ws;

import ru.prbb.common.exception.StandardException;

import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.WebFault;
import java.io.Serializable;

/**
 * Класс для возврата сообщения об ошибке из SOAP-сервисов. Возвращает клиенту простое текстовое сообщение с описанием ошибки.
 *
 * @author lesinsa
 */
@WebFault
public class OperationFault extends StandardException {
    private CheckFaultBean faultInfo;

    @SuppressWarnings("UnusedDeclaration")
    public OperationFault(String message, CheckFaultBean faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    @SuppressWarnings("UnusedDeclaration")
    public OperationFault(String message, CheckFaultBean faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    @SuppressWarnings("UnusedDeclaration")
    public OperationFault(String message, Throwable cause) {
        this(message, new CheckFaultBean(message), cause);
    }

    @SuppressWarnings("UnusedDeclaration")
    public OperationFault(String message) {
        this(message, new CheckFaultBean(message));
    }

    public CheckFaultBean getFaultInfo() {
        return faultInfo;
    }

    @XmlType
    public static class CheckFaultBean implements Serializable {
        private String message;

        public CheckFaultBean() {
        }

        public CheckFaultBean(String message) {
            this();
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
