package com.urise.webapp.util;

import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    public static void connection(ConnectionFactory connectionFactory, String sql, ExecuteSql executeSql ) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            executeSql.execute(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}

