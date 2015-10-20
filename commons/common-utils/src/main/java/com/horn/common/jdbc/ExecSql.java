package com.horn.common.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author LesinSA
 */
public interface ExecSql<T> {
    T exec(PreparedStatement stmt) throws SQLException;
}
