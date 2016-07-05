package ru.prbb.common.jaxb;

import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.StringWriter;

import static org.junit.Assert.*;

/**
 * @author lesinsa on 20.05.2015.
 */
public class ContextHolderBeanTest {

    public static final String CONTEXT_1 = "CONTEXT_1";
    private ContextHolderBean sut;

    @Before
    public void setUp() throws Exception {
        sut = new ContextHolderBean();
    }

    @Test
    public void shouldReturnNullAtInitialState() throws Exception {
        JAXBContext result = (JAXBContext) sut.get("TEST1");
        assertNull(result);
    }

    @Test
    public void shouldReturnContextWhenExists() throws Exception {
        sut.addContext(CONTEXT_1, SerializerType.XML, TestClass1.class);
        JAXBContext ctx = (JAXBContext) sut.get(CONTEXT_1);
        assertNotNull(ctx);
    }

    @Test
    public void shouldReturnWorkingContext() throws Exception {
        JAXBContext ctx = sut.addContext(CONTEXT_1, SerializerType.XML, TestClass1.class);
        assertNotNull(ctx);

        Marshaller marshaller = ctx.createMarshaller();
        TestClass1 t1 = new TestClass1("param1", "arg1");
        StringWriter writer = new StringWriter();
        marshaller.marshal(t1, writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><testClass1>" +
                "<param>param1</param><arg>arg1</arg></testClass1>", writer.toString());
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TestClass1 {
        private String param;
        private String arg;

        public TestClass1() {
        }

        public TestClass1(String param, String arg) {
            this();
            this.param = param;
            this.arg = arg;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }

        public String getArg() {
            return arg;
        }

        public void setArg(String arg) {
            this.arg = arg;
        }
    }
}