package ru.prbb.common.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author lesinsa on 13.04.14.
 */
public class UrlInfoTest {

    public static final String DATASOURCE_NAME = "jdbc/ureb";
    private static final String TEST_LOCATION_1 = "datasource:" + DATASOURCE_NAME + "?ns=ru.prbb.mobile&tableName=mAppConfig";
    private static final String TEST_LOCATION_2 = "datasource:" + DATASOURCE_NAME;
    private static final String TEST_LOCATION_3 = "datasource:" + DATASOURCE_NAME + "?";
    private static final String TEST_LOCATION_4 = "datasource://" + DATASOURCE_NAME + "?";

    @Test
    public void test1() throws Exception {
        UrlInfo sut = new UrlInfo(TEST_LOCATION_1);
        assertEquals("datasource", sut.getProtocol());
        assertEquals("jdbc/ureb", sut.getResourceName());

        assertEquals(2, sut.getProperties().size());
        assertEquals("ru.prbb.mobile", sut.getProperty("ns"));
        assertEquals("mAppConfig", sut.getProperty("tableName"));
    }

    @Test
    public void test2() throws Exception {
        UrlInfo sut = new UrlInfo(TEST_LOCATION_2);
        assertEquals(0, sut.getProperties().size());
        assertEquals("datasource", sut.getProtocol());
        assertEquals("jdbc/ureb", sut.getResourceName());
    }

    @Test
    public void test3() throws Exception {
        UrlInfo sut = new UrlInfo(TEST_LOCATION_3);
        assertEquals(0, sut.getProperties().size());
        assertEquals("datasource", sut.getProtocol());
        assertEquals("jdbc/ureb", sut.getResourceName());
    }

    @Test(expected = ConfigException.class)
    public void test4() throws Exception {
        UrlInfo sut = new UrlInfo("jdbc/test");
        assertEquals(0, sut.getProperties().size());
        assertEquals("datasource", sut.getProtocol());
        assertEquals("jdbc/ureb", sut.getResourceName());
    }

    @Test
    public void test5() throws Exception {
        UrlInfo sut = new UrlInfo(TEST_LOCATION_3 + "table_name=Таблица1&ns=ru.prbb.mobile");
        assertEquals("datasource", sut.getProtocol());
        assertEquals("jdbc/ureb", sut.getResourceName());
        assertEquals(2, sut.getProperties().size());
        assertEquals("Таблица1", sut.getProperty("table_name"));
        assertEquals("ru.prbb.mobile", sut.getProperty("ns"));
    }

    @Test
    public void test6() throws Exception {
        UrlInfo sut = new UrlInfo(TEST_LOCATION_4 + "table_name=Таблица1&ns=ru.prbb.mobile");
        assertEquals("datasource", sut.getProtocol());
        assertEquals("jdbc/ureb", sut.getResourceName());
        assertEquals(2, sut.getProperties().size());
        assertEquals("Таблица1", sut.getProperty("table_name"));
        assertEquals("ru.prbb.mobile", sut.getProperty("ns"));
    }
}
