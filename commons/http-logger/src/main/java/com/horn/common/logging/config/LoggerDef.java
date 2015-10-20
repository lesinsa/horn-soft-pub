package com.horn.common.logging.config;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by lesinsa on 15.10.2015.
 */
@XmlType(name = "logger")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoggerDef {
    @XmlAttribute(name = "path")
    private String path;
    @XmlAttribute(name = "method")
    private String method;
    @XmlAttribute(name = "consumes")
    private String consumes;
    @XmlAttribute(name = "disable-request-body")
    private boolean disableRequestBody;
    @XmlAttribute(name = "disable-response-body")
    private boolean disableResponseBody;
    @XmlElement(name = "appender-ref")
    private List<AppenderRef> appenderRefs;
    @XmlElementWrapper(name = "content-filters")
    @XmlElement(name = "filter")
    private List<ContentFilterDef> contentFilters;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getConsumes() {
        return consumes;
    }

    public void setConsumes(String consumes) {
        this.consumes = consumes;
    }

    public boolean isDisableResponseBody() {
        return disableResponseBody;
    }

    public void setDisableResponseBody(boolean disableResponseBody) {
        this.disableResponseBody = disableResponseBody;
    }

    public List<AppenderRef> getAppenderRefs() {
        if (appenderRefs == null) {
            appenderRefs = new ArrayList<>();
        }
        return appenderRefs;
    }

    public List<ContentFilterDef> getContentFilters() {
        if (contentFilters == null) {
            contentFilters = new ArrayList<>();
        }
        return contentFilters;
    }

    public boolean isDisableRequestBody() {
        return disableRequestBody;
    }

    public void setDisableRequestBody(boolean disableRequestBody) {
        this.disableRequestBody = disableRequestBody;
    }
}
