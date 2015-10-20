package com.horn.common.logging.internal;

import org.junit.Before;
import org.junit.Test;
import com.horn.common.logging.appenders.JdbcAppender;
import com.horn.common.logging.appenders.NopLogAppender;
import com.horn.common.logging.config.HttpLogConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author by lesinsa on 18.10.2015.
 */
public class LoggersTest {

    private Loggers sut;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        try (InputStream inputStream = LoggersTest.class.getResourceAsStream("/example-config1.xml")) {
            HttpLogConfiguration configuration = ConfigurationLoader.load(inputStream);
            sut = new Loggers(configuration);
        }
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void test1() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getServletPath()).thenReturn("/path3");
        when(request.getContentType()).thenReturn("application/json");

        HttpLogger logger = sut.match(request);

        assertNotNull(logger);
        assertEquals(2, logger.getAppenders().size());
    }

    @Test
    public void test2() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getServletPath()).thenReturn("/path3");
        when(request.getContentType()).thenReturn("application/x-www-form-urlencoded");

        HttpLogger logger = sut.match(request);

        assertNotNull(logger);
        assertEquals(1, logger.getAppenders().size());
        assertEquals(JdbcAppender.class, logger.getAppenders().get(0).getClass());
    }

    @Test
    public void test3() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getServletPath()).thenReturn("/path3");
        when(request.getContentType()).thenReturn("text/xml");

        HttpLogger logger = sut.match(request);

        assertNotNull(logger);
        assertEquals(1, logger.getAppenders().size());
        assertEquals(NopLogAppender.class, logger.getAppenders().get(0).getClass());
    }
}