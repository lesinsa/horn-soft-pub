package ru.prbb.common.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Timestamp;

/**
 * @author lesinsa
 */
public class TimestampLongAdapter extends XmlAdapter<Long, Timestamp> {

    @Override
    public Timestamp unmarshal(Long v) throws Exception {
        return v != null ? new Timestamp(v) : null;
    }

    @Override
    public Long marshal(Timestamp v) throws Exception {
        return v != null ? v.getTime() : null;
    }
}
