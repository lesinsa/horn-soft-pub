package ru.prbb.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.prbb.common.jdbc.JdbcHelper;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.*;

import static ru.prbb.common.jdbc.JdbcHelper.*;

/**
 * @author lesinsa on 13.08.14.
 */
@Stateless
public class UserSessionDao {
    public static final String INSERT_USER_SESSION_SQL = "INSERT INTO user_session(id, user_domain, user_id, " +
            "start_time, end_time, app_id, bank_id, custom_data_xml, custom_data_class) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String BASE_SELECT = "SELECT\n" +
            "  id,\n" +
            "  user_domain,\n" +
            "  user_id,\n" +
            "  start_time,\n" +
            "  end_time,\n" +
            "  app_id,\n" +
            "  bank_id,\n" +
            "  custom_data_xml,\n" +
            "  custom_data_class\n" +
            "FROM user_session u\n";
    public static final String FIND_BY_ID_SQL = BASE_SELECT +
            "WHERE id = ? AND user_domain = ?";
    public static final String FIND_LAST_SQL = BASE_SELECT +
            "WHERE user_id = ? AND user_domain = ? AND end_time >= ?\n" +
            " ORDER BY end_time DESC LIMIT 1";
    private static final Logger LOG = LoggerFactory.getLogger(UserSessionDao.class);

    @Resource(lookup = "jdbc/mobile_service")
    private DataSource dataSource;

    public UserSession create(final UserSession session) {
        return JdbcHelper.executeSql(dataSource, INSERT_USER_SESSION_SQL, stmt -> {
            setString(stmt, 1, session.getId());
            setString(stmt, 2, session.getUserDomain());
            setInt(stmt, 3, session.getUserId());
            setTimestamp(stmt, 4, session.getStartTime());
            setTimestamp(stmt, 5, session.getEndTime());
            setString(stmt, 6, session.getAppId());
            setInt(stmt, 7, session.getBankId());
            mapCustomData(stmt, session);
            stmt.executeUpdate();
            return session;
        });
    }

    public UserSession findById(final String id, final String domain) {
        return JdbcHelper.executeSql(dataSource, FIND_BY_ID_SQL, stmt -> {
            setString(stmt, 1, id);
            setString(stmt, 2, domain);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapUserSession(rs);
            } else {
                LOG.debug("Session not found: {}", id);
                return null;
            }
        });
    }

    public UserSession findLastByUserId(final Integer userId, final String userDomain, final Timestamp atTime) {
        return JdbcHelper.executeSql(dataSource, FIND_LAST_SQL, stmt -> {
            setInt(stmt, 1, userId);
            setString(stmt, 2, userDomain);
            setTimestamp(stmt, 3, atTime);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapUserSession(rs);
            } else {
                LOG.debug("Session for userId = {}, domain = {}, at time = {} is not found", userId, userDomain, atTime);
                return null;
            }
        });
    }

    public UserSession updateEndTime(final UserSession session, final Timestamp endTime) {
        JdbcHelper.executeSql(dataSource, "UPDATE user_session SET end_time = ? WHERE id = ?", stmt -> {
            setTimestamp(stmt, 1, endTime);
            setString(stmt, 2, session.getId());
            int i = stmt.executeUpdate();
            if (i == 0) {
                LOG.debug("Session is not updated: {}", session.getId());
            }
            session.setEndTime(endTime);
            return null;
        });
        return session;
    }

    private UserSession mapUserSession(ResultSet rs) throws SQLException {
        UserSession session = new UserSession();
        session.setId(rs.getString("id"));
        session.setUserDomain(rs.getString("user_domain"));
        session.setUserId(rs.getInt("user_id"));
        session.setStartTime(rs.getTimestamp("start_time"));
        session.setEndTime(rs.getTimestamp("end_time"));
        session.setAppId(rs.getString("app_id"));
        session.setBankId(rs.getInt("bank_id"));
        String className = rs.getString("custom_data_class");
        if (className != null) {
            try {
                Class<?> clazz = Class.forName(className);
                Object customData = unMarshalCustomData(rs.getString("custom_data_xml"), clazz);
                session.setCustomData(customData);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Cannot find custom data class" + className, e);
            }
        }
        return session;
    }

    private String marshalCustomData(Object customData) {
        try {
            JAXBContext context = JAXBContext.newInstance(customData.getClass());
            Marshaller marshaller = context.createMarshaller();
            StringWriter writer = new StringWriter();
            marshaller.marshal(customData, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Couldn't marshall custom data", e);
        }
    }

    private Object unMarshalCustomData(String xml, Class clazz) {
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller marshaller = context.createUnmarshaller();
            return marshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Couldn't marshall custom data", e);
        }
    }

    private void mapCustomData(PreparedStatement stmt, UserSession session) throws SQLException {
        Object customData = session.getCustomData();
        if (customData != null) {
            String customDataXml = marshalCustomData(customData);
            setString(stmt, 8, customDataXml);
            setString(stmt, 9, customData.getClass().getName());
        } else {
            stmt.setNull(8, Types.VARCHAR);
            stmt.setNull(9, Types.VARCHAR);
        }
    }
}
