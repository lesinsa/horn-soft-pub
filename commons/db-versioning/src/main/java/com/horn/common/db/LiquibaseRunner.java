package com.horn.common.db;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lesinsa
 *         Date: 1/26/14
 *         Time: 6:19 PM
 */
public abstract class LiquibaseRunner {

    public static final String DEFAULT_MASTERLOG_LOCATION = "META-INF/liquibase/masterChangeLog.xml";
    private static final Logger LOGGER = LoggerFactory.getLogger(LiquibaseRunner.class);

    @PostConstruct
    public void init() {
        LOGGER.info("Starting database schema update");
        try {
            try (Connection connection = getDataSource().getConnection()) {
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(getClass().getClassLoader());
                Liquibase liquibase = new Liquibase(getChangeLogFile(), resourceAccessor, database);
                liquibase.update(new Contexts());
                LOGGER.info("Liquibase has done his job.");
                LOGGER.info("Liquibase goes to sleep...");
            }
        } catch (LiquibaseException | SQLException e) {
            LOGGER.error("", e);
            throw new IllegalStateException(e);
        }
    }

    public String getChangeLogFile() {
        return DEFAULT_MASTERLOG_LOCATION;
    }

    public abstract DataSource getDataSource();
}
