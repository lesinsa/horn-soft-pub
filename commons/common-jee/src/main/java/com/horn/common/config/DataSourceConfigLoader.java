package com.horn.common.config;

import com.horn.common.jdbc.JdbcHelper;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.Properties;

import static com.horn.common.NullUtils.coalesce;

/**
 * @author lesinsa on 23.03.14.
 */
@ConfigProtocol(DataSourceConfigLoader.PROTOCOL)
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class DataSourceConfigLoader implements ConfigLoader {
    public static final String PROTOCOL = "datasource";
    public static final String SELECT_SQL = "SELECT %s, %s FROM %s WHERE ns = ?";
    private static final String DEFAULT_CONFIG_TABLE = "mAppConfig";

    @Override
    public Properties load(UrlInfo urlInfo) {
        final String ns = urlInfo.getProperties().getProperty("ns");
        String tableName = coalesce(urlInfo.getProperty("tableName"), DEFAULT_CONFIG_TABLE);
        String keyColumnName = coalesce(urlInfo.getProperty("keyColumnName"), "param_name");
        String valueColumnName = coalesce(urlInfo.getProperty("valueColumnName"), "param_value");

        try {
            DataSource dataSource = InitialContext.doLookup(urlInfo.getResourceName());
            String selectSql = getSelectSql(tableName, keyColumnName, valueColumnName);
            return JdbcHelper.executeSql(dataSource, selectSql,
                    stmt -> {
                        stmt.setString(1, ns);

                        try (ResultSet rs = stmt.executeQuery()) {
                            Properties properties = new Properties();
                            while (rs.next()) {
                                String key = rs.getString(1);
                                String value = rs.getString(2);
                                if (value != null) {
                                    properties.setProperty(key, value);
                                }
                            }
                            return properties;
                        }
                    });
        } catch (NamingException e) {
            throw new ConfigException("Data source '" + urlInfo.getResourceName() + "' is not found", e);
        }
    }

    private String getSelectSql(String tableName, String keyColumnName, String valueColumnName) {
        return String.format(SELECT_SQL, keyColumnName, valueColumnName, tableName);
    }
}
