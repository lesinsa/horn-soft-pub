package com.horn.common.it;

import com.horn.common.it.samples.*;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.horn.common.jaxb.ContextHolderBean;
import com.horn.common.jaxb.JAXBFactory;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.PrintStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author lesinsa on 20.05.2015.
 */
@RunWith(Arquillian.class)
public class SerializationContextIT {

    @Inject
    private ContextHolderBean holderBean;

    @Inject
    private JAXBFactory jaxbFactory;

    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(WebArchive.class, "serialization.war")
                .addPackage("com.horn.common.jaxb")
                .addClass(SampleContextRepository.class)
                .addClass(BaseSampleRepo.class)
                .addAsWebInfResource(new File("src/main/resources/META-INF/beans.xml"), "beans.xml")
                .addAsResource(new File("src/main/resources/META-INF/services"), "META-INF/services")
                ;
    }

    @Test
    public void test1() throws Exception {
        Object context1 = holderBean.get(SampleContextRepository.CONTEXT_1);
        assertNotNull(context1);
        Object context2 = holderBean.get(SampleContextRepository.CONTEXT_2);
        assertNotNull(context2);
        Object context3 = holderBean.get(SampleContextRepository.CONTEXT_3);
        assertNotNull(context3);
        Object context4 = holderBean.get(SampleContextRepository.CONTEXT_4);
        assertNotNull(context4);

        marshal(context1, new Class1());
        marshal(context2, new Class2());
        marshal(context3, new Class3());

        marshal(context4, new Class1());
        marshal(context4, new Class2());
        marshal(context4, new Class3());
    }

    @Test
    public void test2() throws Exception {
        String xml = jaxbFactory.serialize(SampleContextRepository.CONTEXT_3, new Class3());
        Object result = jaxbFactory.deserialize(SampleContextRepository.CONTEXT_3, xml);
        assertTrue(result.getClass() == Class3.class);
    }

    private void marshal(Object context, Object obj) throws JAXBException {
        Marshaller marshaller = ((JAXBContext) context).createMarshaller();
        marshaller.marshal(obj, new PrintStream(System.out));
        System.out.println();
    }
}
