package me.shamanov.resumedb.storage;

import me.shamanov.resumedb.model.*;
import me.shamanov.resumedb.storage.sql.SqlUtil;
import me.shamanov.resumedb.utils.JSONUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author: Mike
 * Date: 09.04.2019
 */

public class SqlStorage implements Storage {
    private Logger logger = Logger.getLogger(getClass().getName());
    private SqlUtil sqlUtil;

    //Statements
    //INSERT
    private static final String INSERT_RESUME = "INSERT INTO resume (id, full_name, location, age) VALUES (?, ?, ?, ?)";
    private static final String INSERT_CONTACT = "INSERT INTO contact (resume_id, type, value) VALUES (?, ?, ?)";
    private static final String INSERT_SECTION = "INSERT INTO section (resume_id, type, content) VALUES (?, ?, ?)";

    //DELETE
    private static final String DELETE_RESUME = "DELETE FROM resume WHERE id = ?";
    private static final String DELETE_CONTACT = "DELETE FROM contact WHERE resume_id = ?";
    private static final String DELETE_SECTION = "DELETE FROM section WHERE resume_id = ?";
    private static final String DELETE_ALL_RESUMES = "DELETE FROM resume";

    //SELECT
    private static final String COUNT_RESUMES = "SELECT Count(*) FROM resume";
    private static final String SELECT_ALL_SORTED = "SELECT * FROM resume ORDER BY full_name, id";
    private static final String SELECT_RESUME = "SELECT * FROM resume WHERE id = ?";
    private static final String SELECT_CONTACT = "SELECT * FROM contact WHERE resume_id = ?";
    private static final String SELECT_SECTION = "SELECT * FROM section WHERE resume_id = ?";

    //UPDATE
    private static final String UPDATE_RESUME = "UPDATE resume SET full_name = ?, location = ?, age = ? WHERE id = ?";

    public SqlStorage(Properties properties) {
        //loading jdbc driver and checking for validation
        String driver = "[unknown]";
        try {
            driver = properties.getProperty("jdbc.driver");
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            String msg = "Sql driver " + driver + " couldn't be loaded!";
            logThrowing(msg);
        }
        sqlUtil = new SqlUtil(properties);
    }

    @Override
    public Resume load(String id) {
        return sqlUtil.executeTransaction(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(SELECT_RESUME)) {
                ps.setString(1, id);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    String msg = "There is no such a resume with id# " + id + " in the database!";
                    logThrowing(msg, null, IllegalArgumentException.class);
                }
                String fullName = rs.getString("full_name");
                String location = rs.getString("location");
                int age = rs.getInt("age");
                Resume resume = Resume.of(id, fullName, location, age);
                loadContacts(conn, resume);
                loadSections(conn, resume);
                return resume;
            } catch (SQLException e) {
                String msg = "Something has gone wrong while loading Resume with id# " + id + ",\n(SQL error code: " + e.getErrorCode() + ")";
                logThrowing(msg, e);
            }
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        sqlUtil.executeTransaction(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(INSERT_RESUME)) {
                ps.setString(1, resume.getId());
                ps.setString(2, resume.getFullName());
                ps.setString(3, resume.getLocation());
                ps.setInt(4, resume.getAge());
                ps.execute();
                insertContacts(conn, resume);
                insertSections(conn, resume);
            } catch (SQLException e) {
                String msg = "Something has gone wrong while saving Resume: " + resume + ",\n(SQL error code: " + e.getErrorCode() + ")";
                logThrowing(msg, e);
            }
            return null;
        });
    }

    @Override
    public void update(Resume resume) {
        sqlUtil.executeTransaction(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(UPDATE_RESUME)) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getLocation());
                ps.setInt(3, resume.getAge());
                ps.setString(4, resume.getId());
                if (ps.executeUpdate() == 0) {
                    logThrowing("There is no such resume with id# " + resume.getId() + " in the database!");
                }
                deleteContacts(conn, resume);
                deleteSections(conn, resume);
                insertContacts(conn, resume);
                insertSections(conn, resume);
                ps.executeBatch();
            } catch (SQLException e) {
                String msg = "Something has gone wrong while updating Resume: " + resume + ",\n(SQL error code: " + e.getErrorCode() + ")";
                logThrowing(msg, e);
            }
            return null;
        });
    }

    @Override
    public void delete(String id) {
        sqlUtil.executeStatement(DELETE_RESUME, ps -> {
            try {
                ps.setString(1, id);
                if (ps.executeUpdate() == 0)
                    throw new IllegalArgumentException("There is no such resume with id#" + id + " in the database!");
            } catch (SQLException e) {
                String msg = "Something has gone wrong while deleting Resume with id# " + id + ",\n(SQL error code: " + e.getErrorCode() + ")";
                logThrowing(msg, e);
            }
            return null;
        });
    }

    private void deleteContacts(Connection conn, Resume resume) {
        try {
            deleteAllByQuery(conn, resume, DELETE_CONTACT);
        } catch (SQLException e) {
            String msg = "Something has gone wrong while deleting contacts: " + resume + ",\n(SQL error code: " + e.getErrorCode() + ")";
            logThrowing(msg, e);
        }
    }

    private void deleteSections(Connection conn, Resume resume) {
        try {
            deleteAllByQuery(conn, resume, DELETE_SECTION);
        } catch (SQLException e) {
            String msg = "Something has gone wrong while deleting sections: " + resume + ",\n(SQL error code: " + e.getErrorCode() + ")";
            logThrowing(msg, e);
        }
    }

    private void deleteAllByQuery(Connection conn, Resume resume, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, resume.getId());
            ps.execute();
        }
    }

    private void insertContacts(Connection conn, Resume resume) {
        try (PreparedStatement ps = conn.prepareStatement(INSERT_CONTACT)) {
            for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getId());
                ps.setString(2, entry.getKey().name());
                ps.setString(3, entry.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            String msg = "Something has gone wrong while inserting contacts: " + resume + ",\n(SQL error code: " + e.getErrorCode() + ")";
            logThrowing(msg, e);
        }
    }

    private void insertSections(Connection conn, Resume resume) {
        try (PreparedStatement ps = conn.prepareStatement(INSERT_SECTION)) {
            for (Map.Entry<SectionType, Holder> entry : resume.getSections().entrySet()) {
                ps.setString(1, resume.getId());
                ps.setString(2, entry.getKey().name());
                Type type = null;
                switch (entry.getKey()) {
                    case EDUCATION:
                    case EXPERIENCE:
                        type = EstablishmentHolder.class;
                        break;
                    case POSITION:
                    case ACCOMPLISHMENT:
                    case QUALITY:
                        type = MultipleTextHolder.class;
                        break;
                }
                ps.setString(3, JSONUtil.serialize(entry.getValue(), Objects.requireNonNull(type, "Type is null and must be specified!")));
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            String msg = "Something has gone wrong while inserting sections: " + resume + ",\n(SQL error code: " + e.getErrorCode() + ")";
            logThrowing(msg, e);
        }
    }

    private void loadContacts(Connection conn, Resume resume) {
        try (PreparedStatement ps = conn.prepareStatement(SELECT_CONTACT)) {
            ps.setString(1, resume.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String contactType = rs.getString("type");
                String contactValue = rs.getString("value");
                resume.addContact(ContactType.valueOf(contactType), contactValue);
            }
        } catch (SQLException e) {
            String msg = "Something has gone wrong while loading contacts for Resume with id# " + resume.getId() + ",\n(SQL error code: " + e.getErrorCode() + ")";
            logThrowing(msg, e);
        }
    }

    private void loadSections(Connection conn, Resume resume) {
        try (PreparedStatement ps = conn.prepareStatement(SELECT_SECTION)) {
            ps.setString(1, resume.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String sType = rs.getString("type");
                String holder = rs.getString("content");
                SectionType sectionType = SectionType.valueOf(sType);
                Type type = null;
                switch (sectionType) {
                    case EDUCATION:
                    case EXPERIENCE:
                        type = EstablishmentHolder.class;
                        break;
                    case POSITION:
                    case ACCOMPLISHMENT:
                    case QUALITY:
                        type = MultipleTextHolder.class;
                        break;
                }
                resume.addSection(sectionType, JSONUtil.deserialize(holder, Objects.requireNonNull(type, "Type is null and must be specified!")));
            }
        } catch (SQLException e) {
            String msg = "Something has gone wrong while loading sections for Resume with id# " + resume.getId() + ",\n(SQL error code: " + e.getErrorCode() + ")";
            logThrowing(msg, e);
        }
    }

    @Override
    public List<Resume> getSortedResumeList() {
        return sqlUtil.executeTransaction(conn -> {
            Map<String, Resume> resumes = new LinkedHashMap<>();
            try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SORTED)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String id = rs.getString("id");
                    String fullName = rs.getString("full_name");
                    String location = rs.getString("location");
                    int age = rs.getInt("age");
                    resumes.put(id, Resume.of(id, fullName, location, age));
                    loadContacts(conn, resumes.get(id));
                    loadSections(conn, resumes.get(id));
                }
            } catch (SQLException e) {
                String msg = "Something has gone wrong while getting a sorted list of resumes!\n(SQL error code: " + e.getErrorCode() + ")";
                logThrowing(msg, e);
            }
            return new ArrayList<>(resumes.values());
        });
    }

    @Override
    public int size() {
        return sqlUtil.executeStatement(COUNT_RESUMES, ps -> {
            try {
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                    return rs.getInt(1);
            } catch (SQLException e) {
                String msg = "Something has gone wrong while getting a size of storage (number of resumes stored)!\n(SQL error code: " + e.getErrorCode() + ")";
                logThrowing(msg, e);
            }
            return 0;
        });
    }

    @Override
    public void clear() {
        sqlUtil.executeStatement(DELETE_ALL_RESUMES, ps -> {
            try {
                ps.execute();
            } catch (SQLException e) {
                String msg = "Something has gone wrong while clearing the storage (deleting all of the resumes stored)!\n(SQL error code: " + e.getErrorCode() + ")";
                logThrowing(msg, e);
            }
            return null;
        });
    }

    private void logThrowing(String msg) {
        logger.log(Level.SEVERE, msg);
        throw new IllegalStateException(msg);
    }

    private void logThrowing(String msg, Exception e) {
        logger.log(Level.SEVERE, msg, e);
        throw new IllegalStateException(msg, e);
    }

    private <T extends Exception> void logThrowing(String msg, Exception e, Class<T> cover) {
        logger.log(Level.SEVERE, msg, e);
        Exception toThrow = new IllegalStateException();
        try {
            toThrow = cover.getConstructor(String.class).newInstance(msg);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e1) {
            logThrowing("Couldn't instantiate " + cover.getName() + "!", e1);
        }
        toThrow.initCause(e);
        throw (RuntimeException) toThrow;
    }
}
