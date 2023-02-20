package com.urise.webapp.storage;

import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;


    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.connection("DELETE from resume", PreparedStatement::execute);
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume(uuid, full_name) VALUES (?, ?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            insertContacts(conn, r);
            insertSections(conn, r);
            return null;
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException("ERROR: Resume " + r.getUuid() + " doesn't exist");
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")) {
                ps.setString(1, r.getUuid());
                ps.execute();
            }
            insertContacts(conn, r);
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM section WHERE resume_uuid = ?")) {
                ps.setString(1, r.getUuid());
                ps.execute();
            }
            insertSections(conn, r);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.connection("" +
                        "SELECT * FROM resume r " +
                        "  LEFT JOIN contact c " +
                        "    ON r.uuid = c.resume_uuid " +
                        "  LEFT JOIN section ts " +
                        "    ON r.uuid = ts.resume_uuid" +
                        " WHERE  r.uuid=? ",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    do {
                        setContact(rs, r);
                        setSection(rs, r);
                    } while (rs.next());
                    return r;
                });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.connection("DELETE FROM resume WHERE uuid = ?", ps -> {
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
        sqlHelper.connection("" +
                        "SELECT * FROM resume " +
                        " ORDER BY full_name, uuid "
                , ps -> {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        String uuid = rs.getString("uuid");
                        Resume resume = new Resume(uuid, rs.getString("full_name"));
                        sqlHelper.connection("" +
                                " SELECT * FROM contact " +
                                " WHERE resume_uuid = ? ", ps1 -> {
                            ps1.setString(1, uuid);
                            ResultSet rs1 = ps1.executeQuery();
                            while (rs1.next()) {
                                setContact(rs1, resume);
                            }
                            return null;
                        });
                        sqlHelper.connection("" +
                                " SELECT * FROM section " +
                                " WHERE resume_uuid = ?", ps2 -> {
                            ps2.setString(1, uuid);
                            ResultSet rs2 = ps2.executeQuery();
                            while (rs2.next()) {
                                setSection(rs2, resume);
                            }
                            return null;
                        });
                        list.add(resume);
                    }
                    return null;
                });
        return list;
    }

    @Override
    public int size() {
        return sqlHelper.connection("SELECT COUNT(*) from resume", ps -> {
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next() ? resultSet.getInt(1) : 0;
        });
    }

    private void setContact(ResultSet rs, Resume resume) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            ContactType type = ContactType.valueOf(rs.getString("type"));
            resume.setContact(type, value);
        }
    }

    private void setSection(ResultSet rs, Resume resume) throws SQLException {
        String content = rs.getString("content");
        if (content != null) {
            SectionType type = SectionType.valueOf(rs.getString("section_type"));
            if (type.equals(SectionType.PERSONAL) || type.equals(SectionType.OBJECTIVE)) {
                Section section = new TextSection(content);
                resume.setSection(type, section);
            } else if (type.equals(SectionType.ACHIEVEMENT) || type.equals(SectionType.QUALIFICATIONS)) {
                Section section = new ListSection(content.lines().toList());
                resume.setSection(type, section);
            }
        }
    }

    private void insertContacts(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSections(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section(resume_uuid, section_type,  content) VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
                SectionType type = e.getKey();
                if (type.equals(SectionType.PERSONAL) || type.equals(SectionType.OBJECTIVE)) {
                    Section section = e.getValue();
                    ps.setString(1, r.getUuid());
                    ps.setString(2, type.name());
                    ps.setString(3, ((TextSection) section).getContent());
                    ps.addBatch();
                } else if (type.equals(SectionType.ACHIEVEMENT) || type.equals(SectionType.QUALIFICATIONS)) {
                    Section section = e.getValue();
                    ps.setString(1, r.getUuid());
                    ps.setString(2, type.name());
                    StringBuilder content = new StringBuilder();
                    List<String> list = ((ListSection) section).getItems();
                    for (int i = 0; i < list.size(); i++) {
                        content.append(list.get(i)).append("/n");
                    }
                    ps.setString(3, content.toString());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }
}
