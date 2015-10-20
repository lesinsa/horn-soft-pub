package com.horn.common.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author lesinsa
 */
public abstract class BaseDateXmlAdapter extends XmlAdapter<String, Date> {
    protected final DateFormat df;

    public BaseDateXmlAdapter(String pattern) {
        df = new SimpleDateFormat(pattern);
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        return v != null ? new Date(df.parse(v).getTime()) : null;
    }

    @Override
    public String marshal(Date v) throws Exception {
        return v != null ? df.format(v) : null;
    }
}
