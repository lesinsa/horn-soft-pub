package com.horn.common.db;

import org.junit.Test;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author by lesinsa on 14.08.2015.
 */
public class LiquibaseConfigCollectionTest {
    @Test
    public void test1() throws Exception {
        Properties properties;
        URL url = getClass().getResource("/liquibase.properties");
        try (InputStream resourceAsStream = url.openStream()) {
            properties = new Properties();
            properties.load(resourceAsStream);
        }
        LiquibaseConfigCollection sut = new LiquibaseConfigCollection(properties, url);
        Map<Integer, LiquibaseConfig> map = sut.getConfigMap();
        assertEquals(3, map.size());
        assertItem(map, 1, "/META-INF/liquibase/mobile/masterChangeLog.xml", "jdbc/mobile_service");
        assertItem(map, 2, "/META-INF/liquibase/ureb/masterChangeLog.xml", "jdbc/ureb");
        assertItem(map, 3, "/META-INF/liquibase/pgate/masterChangeLog.xml", "jdbc/payment_gate");
    }

    @Test
    public void test2() throws Exception {
        Properties properties = new Properties();
        URL url = getClass().getResource("/liquibase.properties");
        LiquibaseConfigCollection sut = new LiquibaseConfigCollection(properties, url);
        Map<Integer, LiquibaseConfig> map = sut.getConfigMap();
        assertEquals(0, map.size());
    }

    @Test
    public void test3() throws Exception {
        URL url = getClass().getResource("/liquibase.properties");


    }

    private void assertItem(Map<Integer, LiquibaseConfig> map, int index, String expectedMaster, String expectedDataSource) {
        LiquibaseConfig config1 = map.get(index);
        assertNotNull(config1);
        assertEquals(index, config1.getIndex());
        assertEquals(expectedMaster, config1.getMaster());
        assertEquals(expectedDataSource, config1.getDataSourceName());
    }
}