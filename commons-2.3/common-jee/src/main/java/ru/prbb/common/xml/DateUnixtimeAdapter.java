package ru.prbb.common.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Date;

/**
 * @author KandybaevAA
 */
public class DateUnixtimeAdapter extends XmlAdapter<Long, Date> {

    @Override
    public Date unmarshal(Long v) throws Exception {
        return v != null ? new Date(v*1000) : null;
    }

    @Override
    public Long marshal(Date v) throws Exception {
        return v != null ? v.getTime()/1000 : null;
    }
}
