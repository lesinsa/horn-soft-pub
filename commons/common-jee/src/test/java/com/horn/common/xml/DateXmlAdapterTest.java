package com.horn.common.xml;

import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class DateXmlAdapterTest {

    public static final Calendar TEST_CALENDAR = new GregorianCalendar(2014, Calendar.MAY, 21);
    private BaseDateXmlAdapter sut;

    @Before
    public void setUp() throws Exception {
        sut = new DateXmlAdapter();
    }

    @Test
    public void test1() throws Exception {
        String result = sut.marshal(new Date(TEST_CALENDAR.getTimeInMillis()));
        assertEquals("2014-05-21", result);
    }

    @Test
    public void test2() throws Exception {
        Date result = sut.unmarshal("2014-05-21");
        assertEquals(TEST_CALENDAR.getTimeInMillis(), result.getTime());
    }
}