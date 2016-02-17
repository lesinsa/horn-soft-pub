package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogAppender;
import com.horn.common.logging.HttpLogConfigException;
import com.horn.common.logging.appenders.JdbcAppender;
import com.horn.common.logging.config.AppenderDef;
import com.horn.common.logging.config.Property;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author by lesinsa on 14.10.2015.
 */
public class AppenderBuilderTest {

    @Test
    public void testJdbcAppender1() throws Exception {
        AppenderDef def = mock(AppenderDef.class);
        when(def.getClazz()).thenReturn(JdbcAppender.class.getName());
        Property dataSourceProperty = new Property(JdbcAppender.DATASOURCE_PARAM, "jdbc/test");
        when(def.getProperties()).thenReturn(Collections.singletonList(dataSourceProperty));
        HttpLogAppender result = AppenderBuilder.build(def);
        assertNotNull(result);
        assertTrue(result.getClass() == JdbcAppender.class);
        JdbcAppender jdbcAppender = (JdbcAppender) result;
        assertEquals(1, jdbcAppender.getProperties().size());
        assertEquals("jdbc/test", jdbcAppender.getProperties().get(JdbcAppender.DATASOURCE_PARAM));
    }

    @Test(expected = HttpLogConfigException.class)
    public void testJdbcAppender2() throws Exception {
        AppenderDef def = new AppenderDef();
        HttpLogAppender result = AppenderBuilder.build(def);
        assertNotNull(result);
        assertTrue(result.getClass() == JdbcAppender.class);
        JdbcAppender jdbcAppender = (JdbcAppender) result;
        assertEquals(1, jdbcAppender.getProperties().size());
        assertEquals("jdbc/test", jdbcAppender.getProperties().get("datasource"));
    }
}