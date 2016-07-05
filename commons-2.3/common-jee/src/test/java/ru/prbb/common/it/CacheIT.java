package ru.prbb.common.it;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.prbb.common.it.mocks.TestCacheConfig;
import ru.prbb.common.it.mocks.TestCachedClass;

import javax.inject.Inject;
import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author lesinsa
 */
@RunWith(Arquillian.class)
public class CacheIT {
    @Inject
    private TestCachedClass cachedClass;

    @Deployment
    public static Archive<?> deploy() {
        File[] libs = Maven.resolver()
                .resolve("net.sf.ehcache:ehcache:jar:2.9.0")
                .withTransitivity()
                .asFile();

        return ShrinkWrap.create(WebArchive.class, "commonjee.war")
                .addClass(TestCacheConfig.class)
                .addClass(TestCachedClass.class)
                .addPackage("ru.prbb.common.cache")
                .addPackage("ru.prbb.common.cdi")
                .addAsWebInfResource(new File("src/test/resources/cache-beans.xml"), "beans.xml")
                .addAsResource(new File("src/main/resources/META-INF/services"), "META-INF/services")
                .addAsLibraries(libs)
                ;
    }

    @Test
    public void test1() throws Exception {
        cachedClass.setStringValue("value1");
        assertEquals("value1", cachedClass.getStringValue());
        cachedClass.setStringValue("value2");
        assertEquals("value1", cachedClass.getStringValue());
    }

    @Test
    public void testNonCachable1() throws Exception {
        TestCachedClass value1 = new TestCachedClass();
        cachedClass.setObjectValue(value1);
        assertEquals(value1, cachedClass.getObjectValue());

        TestCachedClass value2 = new TestCachedClass();
        cachedClass.setObjectValue(value2);
        assertEquals(value2, cachedClass.getObjectValue());
    }

    @Test
    public void testNullValue() throws Exception {
        assertNull(cachedClass.getNullValue());
        assertNull(cachedClass.getNullValue());
    }
}
