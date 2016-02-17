package it.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author by lesinsa on 13.08.2015.
 */
public class TestServlet2 extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(TestServlet2.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(200);
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");

        try {
            DataSource dataSource = InitialContext.doLookup("java:/jdbc/test");
            try (Connection connection = dataSource.getConnection()) {
                try (ResultSet resultSet = connection.createStatement().executeQuery("select * from log_http_request")) {
                    if (resultSet.next()) {
                        resp.getWriter().print(resultSet.getString("id"));
                    }
                }
            }
        } catch (NamingException | SQLException e) {
            LOG.error("", e);
            throw new ServletException(e);
        }
    }
}
