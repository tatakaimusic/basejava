package com.urise.webapp.storage;

import com.urise.webapp.exeption.ExistStorageException;
import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;
import com.urise.webapp.util.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {

    public final ConnectionFactory connectionFactory;
    private int size = 0;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        SqlHelper.connection(connectionFactory, "DELETE from resume", PreparedStatement::execute);
    }

    @Override
    public void save(Resume r) {
        SqlHelper.connection(connectionFactory, "select exists(select * from resume where uuid = ?)", ps -> {
            ps.setString(1, r.getUuid());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getBoolean(1)) {
                    throw new ExistStorageException("ERROR: Resume " + r.getUuid() + " already exist");
                }
            }
        });

        SqlHelper.connection(connectionFactory, "INSERT INTO resume(uuid, full_name) VALUES (?, ?)", ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
        });
    }

    @Override
    public void update(Resume r) {
        delete(r.getUuid());
        SqlHelper.connection(connectionFactory, "INSERT INTO resume (uuid, full_name) values (?, ?) ", ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
        });
    }

    @Override
    public Resume get(String uuid) {
        Resume resume = new Resume();
        SqlHelper.connection(connectionFactory, "SELECT * FROM resume r WHERE  r.uuid=?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            resume.setUuid(uuid);
            resume.setFullName(rs.getString("full_name"));
        });
        return resume;
    }

    @Override
    public void delete(String uuid) {
        SqlHelper.connection(connectionFactory, "select exists(select * from resume where uuid = ?)", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (!rs.getBoolean(1)) {
                    throw new NotExistStorageException("ERROR: Resume " + uuid + " doesn't exist");
                }
            }
        });

        SqlHelper.connection(connectionFactory, "delete from resume where uuid = ?", ps -> {
            ps.setString(1, uuid);
            ps.execute();
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = new ArrayList<>();
        SqlHelper.connection(connectionFactory, "select * from resume order by full_name ASC", ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Resume resume = new Resume(rs.getString("uuid"), rs.getString("full_name"));
                list.add(resume);
            }
        });
        return list;
    }

    @Override
    public int size() {
        SqlHelper.connection(connectionFactory, "select COUNT(*) from resume", ps -> {
            ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            size = resultSet.getInt(1);
        }
        });
        return size;
    }
}
