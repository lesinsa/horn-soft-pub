package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogReader;
import com.horn.common.logging.domain.LogHttpRequest;
import com.horn.common.jdbc.JdbcHelper;
import com.horn.common.logging.domain.LogHttpData;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by lesinsa on 19.10.2015.
 */
public class HttpLogReaderImpl implements HttpLogReader {

    public static final String SELECT_REQUEST_SQL = "select id, " +
            "http_method, http_path, query_params, request_content_type, response_status, response_content_type, " +
            "start_time, end_time, duration, is_error, stacktrace, local_addr, local_port, remote_addr from log_http_request";
    public static final String SELECT_DATA_SQL = "select * from log_http_data";
    private final DataSource dataSource;

    public HttpLogReaderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Все записи. Только для тестирования.
     */
    @Override
    public List<LogHttpRequest> getAllRequests() {

        return JdbcHelper.executeSql(dataSource, SELECT_REQUEST_SQL, stmt -> {
            List<LogHttpRequest> result = new ArrayList<>();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LogHttpRequest request = mapHttpRequest(rs);

                result.add(request);
            }
            return result;
        });
    }

    @Override
    public List<LogHttpData> getAllHttpDatas() {
        return JdbcHelper.executeSql(dataSource, SELECT_DATA_SQL, stmt -> {
            List<LogHttpData> result = new ArrayList<>();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LogHttpData data = mapLogHttpData(rs);
                result.add(data);
            }
            return result;
        });
    }

    public LogHttpRequest mapHttpRequest(ResultSet rs) throws SQLException {
        LogHttpRequest request = new LogHttpRequest();
        request.setId(rs.getString("id"));
        request.setMethod(rs.getString("http_method"));
        request.setHttpPath(rs.getString("http_path"));
        request.setQueryParams(rs.getString("query_params"));
        request.setRequestContentType(rs.getString("request_content_type"));
        request.setResponseStatus(rs.getInt("response_status"));
        request.setResponseContentType(rs.getString("response_content_type"));
        request.setStartTime(rs.getDate("start_time"));
        request.setEndTime(rs.getDate("end_time"));
        request.setDuration(rs.getInt("duration"));
        request.setError(rs.getBoolean("is_error"));
        request.setStacktrace(rs.getString("stacktrace"));
        request.setLocalAddr(rs.getString("local_addr"));
        request.setLocalPort(rs.getInt("local_port"));
        request.setRemoteAddr(rs.getString("remote_addr"));
        return request;
    }

    public LogHttpData mapLogHttpData(ResultSet rs) throws SQLException {
        LogHttpData data = new LogHttpData();
        data.setId(rs.getString("id"));
        data.setCompressed(rs.getBoolean("is_compressed"));
        data.setEncrypted(rs.getBoolean("is_encrypted"));
        data.setRequestCharset(rs.getString("request_charset"));
        data.setResponseCharset(rs.getString("response_charset"));
        data.setRequestBody(rs.getBytes("request_body"));
        data.setResponseBody(rs.getBytes("response_body"));
        return data;
    }
}
