package com.horn.common.logging.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author by lesinsa on 18.10.2015.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class ContentFilterDef {
    @XmlAttribute(name = "request-mime-type")
    private String requestMimeType;
    @XmlAttribute(name = "response-mime-type")
    private String responseMimeType;
    @XmlAttribute(name = "disabled")
    private boolean disabled;
    @XmlAttribute(name = "disable-request-body")
    private boolean disableRequestBody;
    @XmlAttribute(name = "disable-response-body")
    private boolean disableResponseBody;
    @XmlAttribute(name = "initial-request-buffer-size")
    private Integer initialRequestBufferSize;
    @XmlAttribute(name = "max-request-buffer-size")
    private Integer maxRequestBufferSize;
    @XmlAttribute(name = "initial-response-buffer-size")
    private Integer initialResponseBufferSize;
    @XmlAttribute(name = "max-response-buffer-size")
    private Integer maxResponseBufferSize;

    public String getRequestMimeType() {
        return requestMimeType;
    }

    public void setRequestMimeType(String requestMimeType) {
        this.requestMimeType = requestMimeType;
    }

    public String getResponseMimeType() {
        return responseMimeType;
    }

    public void setResponseMimeType(String responseMimeType) {
        this.responseMimeType = responseMimeType;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisableRequestBody() {
        return disableRequestBody;
    }

    public void setDisableRequestBody(boolean disableRequestBody) {
        this.disableRequestBody = disableRequestBody;
    }

    public boolean isDisableResponseBody() {
        return disableResponseBody;
    }

    public void setDisableResponseBody(boolean disableResponseBody) {
        this.disableResponseBody = disableResponseBody;
    }

    public Integer getInitialRequestBufferSize() {
        return initialRequestBufferSize;
    }

    public void setInitialRequestBufferSize(Integer initialRequestBufferSize) {
        this.initialRequestBufferSize = initialRequestBufferSize;
    }

    public Integer getMaxRequestBufferSize() {
        return maxRequestBufferSize;
    }

    public void setMaxRequestBufferSize(Integer maxRequestBufferSize) {
        this.maxRequestBufferSize = maxRequestBufferSize;
    }

    public Integer getInitialResponseBufferSize() {
        return initialResponseBufferSize;
    }

    public void setInitialResponseBufferSize(Integer initialResponseBufferSize) {
        this.initialResponseBufferSize = initialResponseBufferSize;
    }

    public Integer getMaxResponseBufferSize() {
        return maxResponseBufferSize;
    }

    public void setMaxResponseBufferSize(Integer maxResponseBufferSize) {
        this.maxResponseBufferSize = maxResponseBufferSize;
    }
}
