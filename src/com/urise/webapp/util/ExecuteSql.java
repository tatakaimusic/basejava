package com.urise.webapp.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ExecuteSql {
    void execute(PreparedStatement ps) throws SQLException;
}
