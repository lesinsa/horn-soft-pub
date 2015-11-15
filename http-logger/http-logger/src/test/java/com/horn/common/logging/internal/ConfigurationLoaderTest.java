package com.horn.common.logging.internal;

import com.horn.common.logging.appenders.JdbcAppender;
import com.horn.common.logging.config.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author by lesinsa on 15.10.2015.
 */
public class ConfigurationLoaderTest {
    @Test
    public void test1() throws Exception {
        HttpLogConfiguration result = ConfigurationLoader.load(getClass().getResourceAsStream("/example-config1.xml"));
        assertNotNull(result);

        assertEquals(2, result.getAppenderDefs().size());
        AppenderDef appenderDef = result.getAppenderDefs().get(0);
        assertEquals("DB", appenderDef.getName());
        assertEquals(JdbcAppender.class.getName(), appenderDef.getClazz());
        assertEquals(1, appenderDef.getProperties().size());
        assertEquals("data-source", appenderDef.getProperties().get(0).getName());
        assertEquals("java:/jdbc/test", appenderDef.getProperties().get(0).getValue());

        assertEquals(6, result.getLoggerDefs().size());
        LoggerDef loggerDef1 = result.getLoggerDefs().get(0);
        assertEquals("/rs", loggerDef1.getPath());
        assertEquals(1, loggerDef1.getAppenderRefs().size());
        AppenderRef appenderRef1 = loggerDef1.getAppenderRefs().get(0);
        assertEquals(appenderDef, appenderRef1.getAppenderDef());
    }

    @Test
    public void test2() throws Exception {
        HttpLogConfiguration result = ConfigurationLoader.load(getClass().getResourceAsStream("/example-config1.xml"));
        assertNotNull(result);

        List<LoggerDef> loggerDefs = result.getLoggerDefs();
        assertTrue(loggerDefs.size() > 2);

        LoggerDef loggerDef1 = loggerDefs.get(0);
        List<ContentFilterDef> contentFilters1 = loggerDef1.getContentFilters();
        assertEquals(1, contentFilters1.size());
        ContentFilterDef filter1_1 = contentFilters1.get(0);
        assertNull(filter1_1.getRequestMimeType());
        assertEquals("image/*", filter1_1.getResponseMimeType());
        assertTrue(filter1_1.isDisabled());
        assertFalse(filter1_1.isDisableRequestBody());
        assertFalse(filter1_1.isDisableResponseBody());

        LoggerDef loggerDef2 = loggerDefs.get(1);
        List<ContentFilterDef> contentFilters2 = loggerDef2.getContentFilters();
        assertNotNull(contentFilters2);
        assertEquals(2, contentFilters2.size());
        ContentFilterDef filter2_2 = contentFilters2.get(1);
        assertEquals("application/json", filter2_2.getRequestMimeType());
        assertEquals("image/jpeg", filter2_2.getResponseMimeType());
        assertFalse(filter2_2.isDisabled());
        assertTrue(filter2_2.isDisableRequestBody());
        assertTrue(filter2_2.isDisableResponseBody());
        assertEquals(Integer.valueOf(725), filter2_2.getInitialRequestBufferSize());
        assertEquals(Integer.valueOf(1524), filter2_2.getMaxRequestBufferSize());
        assertEquals(Integer.valueOf(872), filter2_2.getInitialResponseBufferSize());
        assertEquals(Integer.valueOf(2524), filter2_2.getMaxResponseBufferSize());

    }
}