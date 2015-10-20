package com.horn.common.logging.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by lesinsa on 26.09.2015.
 */
@XmlRootElement(name = "http-log-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class HttpLogConfiguration {
    @XmlElement(name = "reader-datasource")
    private String readerDataSource;
    @XmlElement(name = "appender")
    private List<AppenderDef> appenderDefs;
    @XmlElement(name = "logger")
    private List<LoggerDef> loggerDefs;

    public String getReaderDataSource() {
        return readerDataSource;
    }

    public void setReaderDataSource(String readerDataSource) {
        this.readerDataSource = readerDataSource;
    }

    public List<AppenderDef> getAppenderDefs() {
        if (appenderDefs == null) {
            appenderDefs = new ArrayList<>();
        }
        return appenderDefs;
    }

    public List<LoggerDef> getLoggerDefs() {
        if (loggerDefs == null) {
            loggerDefs = new ArrayList<>();
        }
        return loggerDefs;
    }
}
