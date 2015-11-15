package com.horn.common.logging.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * @author by lesinsa on 17.10.2015.
 */
public class PrintWriterWrapper extends PrintWriter {

    public PrintWriterWrapper(OutputStream out, String charsetName) {
        super(new DirectOutputStreamWriter(out, charsetName));
    }

    private static class DirectOutputStreamWriter extends Writer {
        private final OutputStream outputStream;
        private final Charset charset;

        private DirectOutputStreamWriter(OutputStream outputStream, String charsetName) {
            this.outputStream = outputStream;
            this.charset = Charset.forName(charsetName);
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            byte[] bytes = new String(cbuf, off, len).getBytes(charset);
            outputStream.write(bytes);
        }

        @Override
        public void flush() throws IOException {
            outputStream.flush();
        }

        @Override
        public void close() throws IOException {
            outputStream.close();
        }
    }
}
