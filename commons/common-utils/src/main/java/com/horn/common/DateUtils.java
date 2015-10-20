package com.horn.common;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import static com.horn.common.NullUtils.coalesce;

/**
 * @author KandybaevAA
 */
public final class DateUtils {
    private DateUtils() {
    }

    public static Date parseDate(String date) throws ParseException {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(date);
        } else {
            return null;
        }
    }

    public static XMLGregorianCalendar convertDate(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(coalesce(date, new Date()));
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        } catch (DatatypeConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static XMLGregorianCalendar convertDate(String dateStr) throws ParseException {
        Date date = parseDate(dateStr);
        return convertDate(date);
    }

    public static Timestamp xmlCalendarToTimestamp(XMLGregorianCalendar calendar) {
        return calendar != null ? new Timestamp(calendar.toGregorianCalendar().getTimeInMillis()) : null;
    }

    public static java.sql.Date xmlCalendarToSQLDate(XMLGregorianCalendar calendar) {
        return calendar != null ? new java.sql.Date(calendar.toGregorianCalendar().getTimeInMillis()) : null;
    }

    public static Date createDate(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
        GregorianCalendar calendar = new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute, second);
        return calendar.getTime();
    }

    public static Date createDate(int year, int month, int dayOfMonth) {
        GregorianCalendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        return calendar.getTime();
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static java.sql.Date nowSqlDate(){
        return new java.sql.Date(Calendar.getInstance().getTimeInMillis());
    }
}
