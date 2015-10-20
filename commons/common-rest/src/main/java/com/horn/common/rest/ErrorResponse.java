package com.horn.common.rest;

import javax.xml.bind.annotation.*;

/**
 * @author lesinsa
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ErrorResponse {
    @XmlElement
    private ErrorWrapper error;

    protected ErrorResponse() {
        // required by JAXB
    }

    public ErrorResponse(String message) {
        this();
        this.error = new ErrorWrapper(message);
    }

    public ErrorResponse(String message, String exceptionClass, String debugDescription) {
        this();
        this.error = new ErrorWrapper(message, exceptionClass, debugDescription);
    }

    public ErrorWrapper getError() {
        return error;
    }

    public void setError(ErrorWrapper error) {
        this.error = error;
    }

    @XmlType(propOrder = {"message", "exceptionClass", "debugDescription"})
    public static class ErrorWrapper {
        private String message;
        private String exceptionClass;
        private String debugDescription;

        public ErrorWrapper() {
        }

        public ErrorWrapper(String message) {
            this.message = message;
        }

        public ErrorWrapper(String message, String exceptionClass, String debugDescription) {
            this();
            this.message = message;
            this.exceptionClass = exceptionClass;
            this.debugDescription = debugDescription;
        }

        @XmlElement(name = "error_message")
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @XmlElement(name = "exception_class")
        public String getExceptionClass() {
            return exceptionClass;
        }

        public void setExceptionClass(String exceptionClass) {
            this.exceptionClass = exceptionClass;
        }

        @XmlElement(name = "debug_desc")
        public String getDebugDescription() {
            return debugDescription;
        }

        public void setDebugDescription(String debugDescription) {
            this.debugDescription = debugDescription;
        }
    }
}
