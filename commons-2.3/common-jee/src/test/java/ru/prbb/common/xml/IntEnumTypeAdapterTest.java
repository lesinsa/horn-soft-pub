package ru.prbb.common.xml;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class IntEnumTypeAdapterTest {

    private IntEnumTypeAdapter<LoginType> sut;

    @Before
    public void setUp() throws Exception {
        sut = new IntEnumTypeAdapter<LoginType>() {

        };
    }

    @Test
    public void testMarshal() throws Exception {
        Integer result = sut.marshal(LoginType.MOBILE);
        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    public void testUnmarshal() throws Exception {
        LoginType result = sut.unmarshal(4);
        Assert.assertEquals(LoginType.FL, result);
    }

    @Test
    public void test3() throws Exception {
        JAXBContext context = JAXBContext.newInstance(TestBean.class);
        Marshaller marshaller = context.createMarshaller();
        StringWriter writer = new StringWriter();
        marshaller.marshal(new TestBean(LoginType.FL), writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><testBean>" +
                "<logintype>4</logintype></testBean>", writer.toString());
    }

    @XmlEnum
    public static enum LoginType {
        @XmlEnumValue("1")
        MOBILE,
        @XmlEnumValue("4")
        FL
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TestBean {
        @XmlElement(name = "logintype")
        @XmlJavaTypeAdapter(LoginTypeAdapter.class)
        private LoginType loginType;

        public TestBean() {
        }

        public TestBean(LoginType loginType) {
            this();
            this.loginType = loginType;
        }
    }

    public static class LoginTypeAdapter extends IntEnumTypeAdapter<LoginType> {
    }
}