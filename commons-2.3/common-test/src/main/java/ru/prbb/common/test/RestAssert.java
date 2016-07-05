package ru.prbb.common.test;

import com.sun.jersey.api.client.ClientResponse;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * <p>Общие проверки для REST-тестов</p>
 *
 * @author lesinsa
 *         14.02.14 18:04
 */
public final class RestAssert {

    public static final MediaType DEFAULT_RESPONSE_TYPE = MediaType.APPLICATION_JSON_TYPE;
    public static final String DEFAULT_CHARSET = "utf-8";

    private RestAssert() {
        // nothing to do
    }

    public static void assertCommonNormalResponse(ClientResponse clientResponse, MediaType applicationJsonType) {
        assertEquals(HttpServletResponse.SC_OK, clientResponse.getStatus());
        assertMediaTypeEquals(clientResponse, applicationJsonType);
    }

    public static void assertCommonNormalResponse(ClientResponse clientResponse) {
        assertClientResponse(clientResponse, HttpServletResponse.SC_OK, DEFAULT_RESPONSE_TYPE);
    }

    public static void assertClientResponse(ClientResponse clientResponse, int expectedStatus, MediaType expectedMediaType) {
        assertEquals(expectedStatus, clientResponse.getStatus());
        assertMediaTypeEquals(clientResponse, expectedMediaType);
    }

    public static void assertClientResponse(ClientResponse clientResponse, int expectedStatus) {
        assertEquals(expectedStatus, clientResponse.getStatus());
    }

    public static void assertMediaTypeEquals(ClientResponse clientResponse, MediaType expected) {
        assertNotNull(clientResponse.getType());
        assertEquals(expected.getType(), clientResponse.getType().getType());
        assertEquals(expected.getSubtype(), clientResponse.getType().getSubtype());
    }

    public static void assertCharset(String expectedCharset, ClientResponse clientResponse) {
        MediaType type = clientResponse.getType();
        String actualCharset = type.getParameters().get("charset");
        actualCharset = actualCharset != null ? actualCharset.toLowerCase() : null;
        assertEquals(expectedCharset, actualCharset);
    }
}
