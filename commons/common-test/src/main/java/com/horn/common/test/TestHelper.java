package com.horn.common.test;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONUnmarshaller;
import org.apache.commons.io.IOUtils;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

/**
 * @author lesinsa
 */
public final class TestHelper {

    private TestHelper() {
        // nothing to do
    }

    public static String readResourceAsString(String name, String charsetName) throws IOException {
        return new String(readResourceAsBytes(name), charsetName);
    }

    public static byte[] readResourceAsBytes(String name) throws IOException {
        InputStream inputStream = TestHelper.class.getResourceAsStream(name);
        if (inputStream == null) {
            throw new IOException("Resource is not available: " + name);
        }
        try {
            return IOUtils.toByteArray(inputStream);
        } finally {
            inputStream.close();
        }
    }

    public static <T> T unmarshalJSON(String json, Class<T> clazz) throws JAXBException {
        JSONConfiguration mapped = JSONConfiguration.mapped()
                .build();
        JSONJAXBContext context = new JSONJAXBContext(mapped, clazz);
        JSONUnmarshaller unmarshaller = context.createJSONUnmarshaller();
        return unmarshaller.unmarshalFromJSON(new StringReader(json), clazz);

    }

    public static void assertResourceEquals(String resourcePath, String content) throws IOException {
        String resource = readResourceAsString(resourcePath, "UTF-8");
        assertEquals(resource, content);
    }
}
