package ru.prbb.common.jdbc;

import java.sql.CallableStatement;
import java.sql.SQLException;

public interface ExecSP<T> {
    T exec(CallableStatement stmt) throws SQLException;
}

