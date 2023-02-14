package com.urise.webapp.sql;

import com.urise.webapp.exeption.ExistStorageException;
import com.urise.webapp.exeption.StorageException;

import java.sql.SQLException;

public class SqlException {
    private SqlException() {
    }

    public static StorageException convertException(SQLException e) {
        if (e.getSQLState().equals("23505")) {
            return new ExistStorageException(null);
        }
        return new StorageException(e);
    }


}
