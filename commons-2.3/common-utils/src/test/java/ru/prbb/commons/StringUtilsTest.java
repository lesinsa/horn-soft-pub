package ru.prbb.commons;

import org.junit.Test;
import ru.prbb.common.StringUtils;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StringUtilsTest {

    @Test
    public void testSecureString1() throws Exception {
        String result = StringUtils.secureString("A9815D10E243AF14CAFD2AD636855497", 5);
        assertEquals("A9815**********************55497", result);
    }

    @Test
    public void testSecureString2() throws Exception {
        String result = StringUtils.secureString("A9815D10E243AF14CAFD2AD636855497", 5, '+');
        assertEquals("A9815++++++++++++++++++++++55497", result);
    }

    @Test
    public void testSecureString3() throws Exception {
        String result = StringUtils.secureString("A981555497", 5, '+');
        assertEquals("A981555497", result);
    }

    @Test
    public void testSecureString4() throws Exception {
        String result = StringUtils.secureString("A9815497", 5, '+');
        assertEquals("A9815497", result);
    }

    @Test
    public void testSecureStringWithReplace1() throws Exception {
        String result = StringUtils.secureStringWithReplace("A9815D10E243AF14CAFD2AD636855497", 5, "***");
        assertEquals("A9815***55497", result);
    }

    @Test
    public void testJoin1() throws Exception {
        String result = StringUtils.join(Arrays.asList("1", "2", "3"), ",");
        assertEquals("1,2,3", result);
    }

    @Test
    public void testLpad1() throws Exception {
        String result = StringUtils.lpad("6", '0', 5);
        assertEquals("00006", result);
    }

    @Test
    public void testLpad2() throws Exception {
        String result = StringUtils.lpad("12345", '0', 5);
        assertEquals("12345", result);
    }

    @Test
    public void testLpad3() throws Exception {
        String result = StringUtils.lpad("123456", '0', 5);
        assertEquals("123456", result);
    }

    @Test
    public void testLpad4() throws Exception {
        String result = StringUtils.lpad(null, '0', 5);
        assertNull(result);
    }
}