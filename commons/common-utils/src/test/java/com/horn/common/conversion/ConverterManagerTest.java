package com.horn.common.conversion;

import org.junit.Before;
import org.junit.Test;
import com.horn.common.conversion.ConversionException;
import com.horn.common.conversion.Converter;
import com.horn.common.conversion.ConverterManager;
import com.horn.common.conversion.ConverterSet;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author lesinsa on 13.04.14.
 */
public class ConverterManagerTest {

    private ConverterManager sut;

    @Before
    public void setUp() {
        sut = ConverterManager.getInstance(ConverterSet.STANDARD);
    }

    @Test
    public void test1() throws Exception {
        ConverterManager sut = ConverterManager.getInstance(ConverterSet.EMPTY);

        sut.register(URLConverter.class);
        assertEquals(URLConverter.class, sut.find(String.class, URL.class));
    }

    @Test
    public void test2() throws Exception {
        ConverterManager sut = ConverterManager.getInstance(ConverterSet.EMPTY);

        sut.register(URLConverter.class);
        URL result = sut.convert("http://ya.ru", URL.class);
        assertEquals("http://ya.ru", result.toString());
    }

    @Test
    public void test3() throws Exception {
        assertEquals(12345, (int) sut.convert("12345", Integer.class));
        assertEquals(12345L, (long) sut.convert("12345", Long.class));
        assertEquals("http://google.com", sut.convert("http://google.com", URL.class).toString());
        assertEquals("http://google.com", sut.convert("http://google.com", String.class));
        assertEquals(new BigDecimal("27.5643"), sut.convert("27.5643", BigDecimal.class));
        assertEquals("test string", sut.convert("test string", String.class));

        Float aFloat = sut.convert("132.5467", Float.class);
        assertTrue(Math.abs(aFloat - 132.5467) < 1E-5);
        Double aDouble = sut.convert("1325.5467", Double.class);
        assertTrue(Math.abs(aDouble - 1325.5467) < 1E-10);
    }

    @Test
    public void test4() throws Exception {
        Date result = sut.convert("", Date.class);
        assertNull(result);
    }

    @Test(expected = ConversionException.class)
    public void test5() throws Exception {
        sut.setEmptyStringAsNull(false);
        sut.convert("", Date.class);
    }

    @Test
    public void test6() throws Exception {
        Boolean result = sut.convert(null, Boolean.class);
        assertNull(result);
    }

    public static class URLConverter implements Converter<String, URL> {

        @Override
        public URL convert(String value) throws ConversionException {
            try {
                return new URL(value);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
