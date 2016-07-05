package ru.prbb.common.it;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.prbb.common.cdi.ProgrammaticCdiBeanLookup;
import ru.prbb.common.config.ConfigBeanPostProcessor;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

/**
 * @author MaslovDV
 */
@RunWith(Arquillian.class)
public class CommonConfigIT {
    public static final String DEF_NS = "ru.prbb.common.soap-config";
    private static boolean init;
    @Inject
    private Instance<SoapConfig> soapConfigInstance;
    @Inject
    private Instance<TestConfigFromFile> configFromFileInstance;

    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class, "commonjee.jar")
                .addClass(ConfigBeanPostProcessor.class)
                .addClass(SoapConfig.class)
                .addClass(TestConfigFromFile.class)
                .addPackage("ru.prbb.common.config")
                .addPackage("ru.prbb.common.cdi")
                .addAsManifestResource(new File("src/main/resources/META-INF/beans.xml"))
                .addAsManifestResource(new File("src/main/resources/META-INF/services"))
                ;
    }

    private static void insertRow(PreparedStatement stmt, String ns, String paramName, String paramValue) throws SQLException {
        stmt.setString(1, ns);
        stmt.setString(2, paramName);
        stmt.setString(3, paramValue);
        stmt.execute();
    }

    @Before
    public void setUp() throws Exception {
        if (init) {
            return;
        }
        try {
            DataSource dataSource = InitialContext.doLookup("jdbc/test");
            try (Connection connection = dataSource.getConnection()) {
                Statement createTableStatement = connection.createStatement();
                createTableStatement.execute("CREATE TABLE mAppConfig(ns VARCHAR(255), param_name VARCHAR(255), param_value VARCHAR(1024))");
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO mAppConfig(ns, param_name, param_value) VALUES(?, ?, ?)");
                insertRow(stmt, DEF_NS, "elifeService", "http://elifebus.life.corp/elife.v1/");
                insertRow(stmt, DEF_NS, "smsService", "http://smsgate.life.corp/smsservice");
                insertRow(stmt, DEF_NS, "soaInfraService", "http://esb.life.corp/service");
                insertRow(stmt, DEF_NS, "crmService", "http://crm.life.corp/clients");
                insertRow(stmt, DEF_NS, "elifeService.v2", "http://elifebus.life.corp/elife.v2/");
                insertRow(stmt, DEF_NS, "session_timeout", "180000");
                insertRow(stmt, DEF_NS, "track_performance", "true");

                insertRow(stmt, "ru.prbb.legacy", "param1", "Hi!");
            }
            init = true;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void test1() throws IllegalAccessException {
        SoapConfig soapConfig = soapConfigInstance.get();
        assertEquals("http://elifebus.life.corp/elife.v1/", soapConfig.getElifeService().toString());
        assertEquals("http://smsgate.life.corp/smsservice", soapConfig.getSmsService().toString());
        assertEquals("http://esb.life.corp/service", soapConfig.getSoaInfraService().toString());
        assertEquals(30000, soapConfig.getWsTimeout());
        assertEquals("http://crm.life.corp/clients", soapConfig.getCrmService().toString());
        assertEquals("http://elifebus.life.corp/elife.v2/", soapConfig.getElifeService2().toString());
        assertEquals(180000, soapConfig.getSessionTimeout());
        assertTrue(soapConfig.isTrackPerformance());
    }

    @Test
    public void test2() throws Exception {
        SoapConfig instance = ProgrammaticCdiBeanLookup.getBeanInstance(SoapConfig.class);
        assertEquals(soapConfigInstance.get(), instance);
    }

    @Test
    public void testConfigFromFile() throws Exception {
        TestConfigFromFile config = configFromFileInstance.get();
        assertEquals("asdfghj", config.getParam1());
    }

    @Test
    public void testProgrammaticLookup() throws Exception {
        SoapConfig result = ProgrammaticCdiBeanLookup.getBeanInstance(SoapConfig.class);
        assertNotNull(result);
    }
}
