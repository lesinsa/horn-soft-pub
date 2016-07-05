package ru.prbb.common.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Date;

/**
 * @author lesinsa
 */
public class DateLongAdapter extends XmlAdapter<Long, Date> {

    @Override
    public Date unmarshal(Long v) throws Exception {
        return v != null ? new Date(v) : null;
    }

    @Override
    public Long marshal(Date v) throws Exception {
        return v != null ? v.getTime() : null;
    }
}
