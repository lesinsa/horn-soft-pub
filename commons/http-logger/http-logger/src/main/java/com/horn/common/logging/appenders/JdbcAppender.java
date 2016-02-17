package com.horn.common.logging.appenders;

import com.horn.common.jdbc.JdbcHelper;
import com.horn.common.logging.HttpLogAppender;
import com.horn.common.logging.HttpLogConfigException;
import com.horn.common.logging.HttpLogContext;
import com.horn.common.logging.config.AppenderDef;
import com.horn.common.logging.config.Property;
import com.horn.common.logging.internal.Utils;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author by lesinsa on 14.10.2015.
 */
public class JdbcAppender implements HttpLogAppender {

    public static final String DATASOURCE_PARAM = "data-source";
    public static final String INSERT_REQUEST_SQL = "INSERT INTO log_http_request(id, " +
            "http_method, http_path, query_params, request_content_type, response_status, response_content_type, " +
            "start_time, end_time, duration, is_error, stacktrace, local_addr, local_port, remote_addr) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String INSERT_DATA_SQL = "insert into log_http_data(id, is_compressed, is_encrypted, " +
            "request_body, request_charset, response_body, response_charset) values(?, ?, ?, ?, ?, ?, ?)";
    public static final int MAX_STACKTRACE_LENGTH = 4096;

    private final Map<String, String> properties;
    private final String dataSourceName;
    private DataSource dataSource;

    public JdbcAppender(AppenderDef def) {
        properties = def.getProperties()
                .stream()
                .collect(Collectors.toMap(Property::getName, Property::getValue));
        dataSourceName = properties.get(DATASOURCE_PARAM);
        // check required properties
        if (dataSourceName == null) {
            throw new HttpLogConfigException("Required param is not present in config: " + DATASOURCE_PARAM);
        }
    }

    @Override
    public void append(HttpLogContext logContext) {
        try {
            DataSource dataSource = takeDataSource();
            try (Connection connection = dataSource.getConnection()) {
                connection.setAutoCommit(false);
                try {
                    JdbcHelper.executeSql(connection, INSERT_REQUEST_SQL, stmt -> {
                        mapLogHttpRequest(logContext, stmt);
                        stmt.executeUpdate();
                        return null;
                    });

                    JdbcHelper.executeSql(connection, INSERT_DATA_SQL, stmt -> {
                        mapLogHttpData(stmt, logContext);
                        stmt.executeUpdate();
                        return null;
                    });

                    connection.commit();
                } catch (SQLException e) {
                    connection.rollback();
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public void mapLogHttpData(PreparedStatement stmt, HttpLogContext logContext) throws SQLException {
        // id
        stmt.setString(1, logContext.getId());
        // is_compress
        // TODO implement compression
        stmt.setBoolean(2, false);
        // TODO implement encryption
        // is_encrypted
        stmt.setBoolean(3, false);
        // request_body
        JdbcHelper.setBytes(stmt, 4, logContext.getRequestBody());
        // request_charset
        JdbcHelper.setString(stmt, 5, logContext.getRequest().getCharacterEncoding());
        // response_body
        JdbcHelper.setBytes(stmt, 6, logContext.getResponseBody());
        // request_charset
        JdbcHelper.setString(stmt, 7, logContext.getResponse().getCharacterEncoding());
    }

    public void mapLogHttpRequest(HttpLogContext logContext, PreparedStatement stmt) throws SQLException {
        HttpServletRequest request = logContext.getRequest();
        HttpServletResponse response = logContext.getResponse();

        // id
        stmt.setString(1, logContext.getId());
        // http_method
        stmt.setString(2, request.getMethod());
        // http_path
        stmt.setString(3, Utils.takePath(request));
        // query_params
        JdbcHelper.setString(stmt, 4, request.getQueryString());
        // request_content_type
        JdbcHelper.setString(stmt, 5, takeContentType(request.getContentType()));
        // response_status
        stmt.setInt(6, response.getStatus());
        // response_content_type
        JdbcHelper.setString(stmt, 7, takeContentType(response.getContentType()));
        // start_time
        long startTime = logContext.getStartTime();
        long currentTime = System.currentTimeMillis();
        int duration = (int) (currentTime - startTime);
        stmt.setTimestamp(8, new Timestamp(startTime));
        // end_time
        stmt.setTimestamp(9, new Timestamp(currentTime));
        // duration
        stmt.setInt(10, duration);
        // is_error
        stmt.setBoolean(11, logContext.getException() != null);
        // stacktrace
        String stacktrace = takeStacktrace(logContext.getException());
        stmt.setString(12, stacktrace);
        // local_addr
        stmt.setString(13, request.getLocalAddr());
        // local_port
        stmt.setInt(14, request.getLocalPort());
        // remote_addr
        stmt.setString(15, request.getRemoteHost());
    }

    public String takeContentType(String contentType) {
        try {
            if (contentType != null) {
                MimeType mimeType = new MimeType(contentType);
                return mimeType.getBaseType();
            }
        } catch (MimeTypeParseException ignored) {

        }
        return contentType;
    }

    private String takeStacktrace(Exception exception) {
        if (exception == null) {
            return null;
        }
        StringWriter writer = new StringWriter(MAX_STACKTRACE_LENGTH);
        exception.printStackTrace(new PrintWriter(writer));
        String s = writer.toString();
        return s.length() <= MAX_STACKTRACE_LENGTH ? s : s.substring(0, MAX_STACKTRACE_LENGTH);
    }

    @Override
    public void append(List<HttpLogContext> logContexts) {

    }

    public Map<String, String> getProperties() {
        return new HashMap<>(properties);
    }

    private DataSource takeDataSource() {
        try {
            if (dataSource == null) {
                dataSource = InitialContext.doLookup(dataSourceName);
            }
        } catch (NamingException e) {
            throw new IllegalStateException("DataSource '" + dataSourceName + "' is not found", e);
        }
        return dataSource;
    }
}
