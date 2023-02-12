package com.urise.webapp.storage;

import com.urise.webapp.exeption.ExistStorageException;
import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {

    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE from resume")) {
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void save(Resume r) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps1 = conn.prepareStatement("select exists(select * from resume where uuid = ?)")) {
            ps1.setString(1, r.getUuid());
            ResultSet rs = ps1.executeQuery();
            while (rs.next()) {
                if (rs.getBoolean(1)) {
                    throw new ExistStorageException("ERROR: Resume " + r.getUuid() + " already exist");
                }
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }


        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps2 = conn.prepareStatement("INSERT INTO resume(uuid, full_name) VALUES (?, ?)")) {
            ps2.setString(1, r.getUuid());
            ps2.setString(2, r.getFullName());

            ps2.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void update(Resume r) {
        delete(r.getUuid());
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps2 = conn.prepareStatement("INSERT INTO resume (uuid, full_name) values (?, ?) ")) {
            ps2.setString(1, r.getUuid());
            ps2.setString(2, r.getFullName());
            ps2.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public Resume get(String uuid) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r WHERE  r.uuid=?")) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            return r;
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void delete(String uuid) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps1 = conn.prepareStatement("select exists(select * from resume where uuid = ?)")) {
            ps1.setString(1, uuid);
            ResultSet rs = ps1.executeQuery();
            while (rs.next()) {
                if (!rs.getBoolean(1)) {
                    throw new NotExistStorageException("ERROR: Resume " + uuid + " doesn't exist");
                }
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("delete from resume where uuid = ?")) {
            ps.setString(1, uuid);
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = new ArrayList<>();
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("select * from resume order by full_name ASC")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Resume resume = new Resume(rs.getString("uuid"), rs.getString("full_name"));
                list.add(resume);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
        return list;
    }

    @Override
    public int size() {
        int size = 0;
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("select COUNT(*) from resume")) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                size = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
        return size;
    }
}
