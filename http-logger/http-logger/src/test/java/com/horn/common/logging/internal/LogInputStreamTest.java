package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogContext;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author by lesinsa on 19.10.2015.
 */
public class LogInputStreamTest {

    public static final String TEST_STR = "Привет, Мевед!";

    @Test
    public void test1() throws Exception {
        HttpLogContext logContext = createBodyConfig(false, 0, 0);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_STR.getBytes(StandardCharsets.UTF_8));

        LogInputStream sut = new LogInputStream(new MyServletInputStream(inputStream), logContext);
        String s = IOUtils.toString(sut, StandardCharsets.UTF_8);

        assertEquals(TEST_STR, s);
        assertNull(sut.getData());
    }

    @Test
    public void test2() throws Exception {
        HttpLogContext logContext = createBodyConfig(true, 100, 100);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_STR.getBytes(StandardCharsets.UTF_8));

        LogInputStream sut = new LogInputStream(new MyServletInputStream(inputStream), logContext);
        String s = IOUtils.toString(sut, StandardCharsets.UTF_8);

        assertEquals(TEST_STR, s);
        assertNotNull(sut.getData());
        assertEquals(TEST_STR, new String(sut.getData(), StandardCharsets.UTF_8));
    }

    @Test
    public void test3() throws Exception {
        HttpLogContext logContext = createBodyConfig(true, 12, 12);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_STR.getBytes(StandardCharsets.UTF_8));

        LogInputStream sut = new LogInputStream(new MyServletInputStream(inputStream), logContext);
        String s = IOUtils.toString(sut, StandardCharsets.UTF_8);

        assertEquals(TEST_STR, s);
        assertNotNull(sut.getData());
        assertEquals("Привет", new String(sut.getData(), StandardCharsets.UTF_8));
    }

    private HttpLogContext createBodyConfig(boolean enabled, int initialBufferSize, int maxBufferSize) {
        HttpLogContext logContext = mock(HttpLogContext.class);
        when(logContext.getRequestBodyConfig()).thenReturn(new LogBodyConfig(enabled, initialBufferSize, maxBufferSize));
        return logContext;
    }

    private static class MyServletInputStream extends ServletInputStream {

        private final InputStream delegate;

        private MyServletInputStream(InputStream inputStream) {
            delegate = inputStream;
        }

        @Override
        public int read() throws IOException {
            return delegate.read();
        }
    }
}