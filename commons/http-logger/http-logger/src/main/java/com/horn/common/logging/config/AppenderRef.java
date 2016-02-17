package com.horn.common.logging.config;

import javax.xml.bind.annotation.*;

/**
 * @author by lesinsa on 05.10.2015.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class AppenderRef {
    @XmlIDREF
    @XmlAttribute(name = "ref")
    private AppenderDef appenderDef;

    public AppenderDef getAppenderDef() {
        return appenderDef;
    }

    public void setAppenderDef(AppenderDef appenderDef) {
        this.appenderDef = appenderDef;
    }
}
