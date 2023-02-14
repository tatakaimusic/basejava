package com.urise.webapp.sql;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T> T connection(String sql, ExecuteSql<T> executeSql) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return executeSql.execute(ps);
        } catch (SQLException e) {
            throw SqlException.convertException(e);
        }
    }

//    public <T> T transactionalExecute(SqlTransaction<T> executor) {
//
//    }
}

