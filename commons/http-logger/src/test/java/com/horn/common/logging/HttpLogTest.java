package com.horn.common.logging;

import com.horn.common.logging.domain.LogHttpRequest;
import com.sun.jersey.api.client.Client;
import it.mock.HelloServlet;
import it.mock.TestServlet1;
import it.mock.TestServlet2;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.horn.common.jdbc.JdbcHelper;
import com.horn.common.logging.domain.LogHttpData;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author by lesinsa on 13.08.2015.
 */
public class HttpLogTest {

    public static final int SERVER_PORT = 8123;
    public static final String DATASOURCE_NAME = "java:/jdbc/test";
    public static final String TEST_LOG_FILTER_NAME = "testLogFilter";
    public static final String TEST_DATA = "{\"text\": \"Привет, мир!\"}";
    private static final String DEFAULT_MASTERLOG_LOCATION = "META-INF/liquibase/common-logger-basic.xml";
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpLogTest.class);
    private static Server server;
    private static WebAppContext context;

    @BeforeClass
    public static void setUpSuite() throws Exception {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.eclipse.jetty.jndi.InitialContextFactory");

        InitialContext ctx = new InitialContext();
        ctx.createSubcontext("java:/jdbc");
        PoolProperties properties = new PoolProperties();
        properties.setDriverClassName("org.h2.Driver");
        properties.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        properties.setUsername("sa");
        properties.setPassword("sa");
        DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource(properties);
        ctx.bind(DATASOURCE_NAME, dataSource);


        server = new Server(SERVER_PORT);

        context = new WebAppContext("/", "/");
        context.setContextPath("/");
        context.addServlet(new ServletHolder(new HelloServlet("Buongiorno Mondo")), "/it/*");
        context.addServlet(TestServlet1.class, "/path1/*");
        context.addServlet(TestServlet2.class, "/path2/*");

        FilterHolder filterHolder = new FilterHolder(HttpLogFilter.class);
        filterHolder.setName(TEST_LOG_FILTER_NAME);
        filterHolder.setInitParameter(HttpLogFilter.CONFIG_PATH_PARAM, "example-config2.xml");
        context.addFilter(filterHolder, "/*", EnumSet.allOf(DispatcherType.class));

        server.setHandler(context);

        server.start();

        LOGGER.info("Starting database schema update");
        try {
            try (Connection connection = dataSource.getConnection()) {
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(HttpServletRequest.class.getClassLoader());
                Liquibase liquibase = new Liquibase(DEFAULT_MASTERLOG_LOCATION, resourceAccessor, database);
                liquibase.update(new Contexts());
                LOGGER.info("Liquibase has done his job.");
                LOGGER.info("Liquibase goes to sleep...");
            }
        } catch (LiquibaseException | SQLException e) {
            LOGGER.error("", e);
            throw new IllegalStateException(e);
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        server.stop();
    }

    @Before
    public void setUp() throws Exception {
        // cleaning all log tables
        DataSource dataSource = InitialContext.doLookup(DATASOURCE_NAME);
        try (Connection connection = dataSource.getConnection()) {
            JdbcHelper.executeSql(connection, "delete from log_http_data", PreparedStatement::executeUpdate);
            JdbcHelper.executeSql(connection, "delete from log_http_request", PreparedStatement::executeUpdate);
        }
    }

    @Test
    public void test1() throws Exception {
        String s = readContent("/path1");
        assertEquals("Привет!", s);
    }

    @Test
    public void test2() throws Exception {
        String s = readContent("/path2");
    }

    @Test
    public void test3() throws Exception {
        Client client = Client.create();
        String result = client.resource("http://localhost:" + SERVER_PORT)
                .queryParam("q", "1")
                .path("path1")
                .entity(TEST_DATA, MediaType.APPLICATION_JSON_TYPE + "; charset=windows-1251")
                .post(String.class);
        assertEquals(TEST_DATA, result);
        HttpLogManager logManager = (HttpLogManager) context.getServletContext()
                .getAttribute(HttpLogFilter.HTTP_LOGGER_MANAGER_BASE + TEST_LOG_FILTER_NAME);

        HttpLogReader logReader = logManager.getLogReader();
        List<LogHttpRequest> requests = logReader.getAllRequests();
        assertEquals(1, requests.size());
        LogHttpRequest request = requests.get(0);

        assertNotNull(request.getId());
        assertEquals("POST", request.getMethod());
        assertEquals("/path1", request.getHttpPath());
        assertEquals("q=1", request.getQueryParams());
        assertEquals("application/json", request.getRequestContentType());
        assertEquals(200, request.getResponseStatus());
        assertEquals("text/plain", request.getResponseContentType());
        assertNotNull(request.getStartTime());
        assertNotNull(request.getEndTime());
        assertFalse(request.isError());

        List<LogHttpData> httpDatas = logReader.getAllHttpDatas();
        assertEquals(1, httpDatas.size());
        LogHttpData httpData = httpDatas.get(0);
        assertEquals(request.getId(), httpData.getId());
        assertEquals("windows-1251", httpData.getRequestCharset());
        assertEquals("utf-8", httpData.getResponseCharset());
        assertEquals(TEST_DATA, new String(httpData.getRequestBody(), httpData.getRequestCharset()));
        assertEquals(TEST_DATA, new String(httpData.getResponseBody(), httpData.getResponseCharset()));
    }

    private String readContent(String path) throws IOException {
        URL url = new URL(String.format("http://localhost:%d%s", SERVER_PORT, path));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        try {
            InputStream content = (InputStream) connection.getContent();
            return IOUtils.toString(content, "UTF-8");
        } finally {
            connection.disconnect();
        }
    }
}
