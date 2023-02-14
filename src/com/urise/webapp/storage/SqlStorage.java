package com.urise.webapp.storage;

import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;


    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.connection("DELETE from resume", PreparedStatement::execute);
    }

    @Override
    public void save(Resume r) {
        sqlHelper.connection("INSERT INTO resume(uuid, full_name) VALUES (?, ?)", ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
            return null;
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.connection("UPDATE resume SET full_name = ? WHERE uuid = ?", ps -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException("ERROR: Resume " + r.getUuid() + " doesn't exist");
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.connection("" +
                "SELECT * FROM resume r " +
                " WHERE  r.uuid=?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.connection("delete from resume where uuid = ?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException("ERROR: Resume " + uuid + " doesn't exist");
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = new ArrayList<>();
        sqlHelper.connection("select * from resume order by full_name ASC", ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Resume resume = new Resume(rs.getString("uuid"), rs.getString("full_name"));
                list.add(resume);
            }
            return null;
        });
        return list;
    }

    @Override
    public int size() {
        return sqlHelper.connection("select COUNT(*) from resume", ps -> {
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next() ? resultSet.getInt(1) : 0;
        });
    }
}
