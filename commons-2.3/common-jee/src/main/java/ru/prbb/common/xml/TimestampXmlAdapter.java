package ru.prbb.common.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author lesinsa
 */
public class TimestampXmlAdapter extends XmlAdapter<String, Timestamp> {
    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

    @Override
    public Timestamp unmarshal(String v) throws Exception {
        return v != null ? new Timestamp(df.parse(v).getTime()) : null;
    }

    @Override
    public String marshal(Timestamp v) throws Exception {
        return v != null ? df.format(v) : null;
    }
}
