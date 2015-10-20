package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogContext;
import org.junit.Test;

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author by lesinsa on 19.10.2015.
 */
public class LogOutputStreamTest {

    public static final String TEST_STR = "Привет-привет, Мевед!!!";

    @Test
    public void test1() throws Exception {
        HttpLogContext logContext = createBodyConfig(false, 0, 0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        LogOutputStream sut = new LogOutputStream(new MyServletOutputStream(out), logContext);
        sut.write(TEST_STR.getBytes(StandardCharsets.UTF_8));

        String s = new String(out.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(TEST_STR, s);
        assertNull(sut.getData());
    }

    @Test
    public void test2() throws Exception {
        HttpLogContext logContext = createBodyConfig(true, 100, 100);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        LogOutputStream sut = new LogOutputStream(new MyServletOutputStream(out), logContext);
        sut.write(TEST_STR.getBytes(StandardCharsets.UTF_8));

        String s = new String(out.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(TEST_STR, s);
        assertNotNull(sut.getData());
        assertEquals(TEST_STR, new String(sut.getData(), StandardCharsets.UTF_8));
    }

    @Test
    public void test3() throws Exception {
        HttpLogContext logContext = createBodyConfig(true, 13, 13);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        LogOutputStream sut = new LogOutputStream(new MyServletOutputStream(out), logContext);
        sut.write(TEST_STR.getBytes(StandardCharsets.UTF_8));

        String s = new String(out.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(TEST_STR, s);
        assertNotNull(sut.getData());
        assertEquals("Привет-", new String(sut.getData(), StandardCharsets.UTF_8));
    }

    private HttpLogContext createBodyConfig(boolean enabled, int initialBufferSize, int maxBufferSize) {
        HttpLogContext logContext = mock(HttpLogContext.class);
        when(logContext.getResponseBodyConfig()).thenReturn(new LogBodyConfig(enabled, initialBufferSize, maxBufferSize));
        return logContext;
    }

    private static class MyServletOutputStream extends ServletOutputStream {
        private final OutputStream delegate;

        private MyServletOutputStream(OutputStream delegate) {
            this.delegate = delegate;
        }

        @Override
        public void write(int b) throws IOException {
            delegate.write(b);
        }
    }
}