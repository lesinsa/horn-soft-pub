package com.horn.common.db;

import com.horn.common.logging.App;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @author by lesinsa on 14.08.2015.
 */
@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class LiquibaseScanner {
    public static final String LIQUIBASE_SCAN_PROPERTY = "liquibase.scan";
    private static final Logger LOG = App.LOG;

    @PostConstruct
    public void init() {
        String scanProperty = System.getProperty(LIQUIBASE_SCAN_PROPERTY);
        if ("false".equals(scanProperty)) {
            LOG.info("Liquibase scanner disabled by system property: '{}'=false", LIQUIBASE_SCAN_PROPERTY);
            return;
        }
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            List<LiquibaseConfig> list = readResources(classLoader);
            // упорядочиваем по индексам в обратном порядке
            Collections.sort(list, new Comparator<LiquibaseConfig>() {
                @Override
                public int compare(LiquibaseConfig o1, LiquibaseConfig o2) {
                    return -new Integer(o1.getIndex()).compareTo(o2.getIndex());
                }
            });

            LOG.info("Applying updates to database");
            for (LiquibaseConfig config : list) {
                LOG.info("Processing config: {}", config);
                try (Connection connection = getConnection(config)) {
                    Database database = DatabaseFactory.getInstance()
                            .findCorrectDatabaseImplementation(new JdbcConnection(connection));
                    ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(classLoader);
                    Liquibase liquibase = new Liquibase(getChangeLogFile(config), resourceAccessor, database);
                    liquibase.update(new Contexts());
                }
            }
            LOG.info("Liquibase has done his job. Liquibase goes to sleep...");
        } catch (IOException | LiquibaseException | SQLException | NamingException e) {
            LOG.error("", e);
            throw new IllegalStateException(e);
        }
    }

    private String getChangeLogFile(LiquibaseConfig config) {
        return config.getMaster();
    }

    private Connection getConnection(LiquibaseConfig config) throws NamingException, SQLException {
        DataSource dataSource = InitialContext.doLookup(config.getDataSourceName());
        return dataSource.getConnection();
    }

    private List<LiquibaseConfig> readResources(ClassLoader classLoader) throws IOException {
        ArrayList<LiquibaseConfig> list = new ArrayList<>();
        Enumeration<URL> resources = classLoader.getResources("liquibase.properties");
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();

            LOG.info("Processing liquibase config resource: {}", url);
            try (InputStream inputStream = url.openStream()) {
                Properties properties = new Properties();
                properties.load(inputStream);
                LiquibaseConfigCollection collection = new LiquibaseConfigCollection(properties, url);
                list.addAll(collection.getConfigMap().values());
            }
        }
        return list;
    }
}
