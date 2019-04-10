package me.shamanov.resumedb.storage.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author: Mike
 * Date: 10.04.2019
 * <p>
 * This class is intended to help to manipulate with the designated Database.
 * See <li>config.properties</li> file to set all the parameters.
 */

public class SqlUtil {
    private String url;
    private String user;
    private String pwd;
    private Logger logger = Logger.getLogger(getClass().getName());

    public SqlUtil(Properties properties) {
        this.url = properties.getProperty("jdbc.url");
        this.user = properties.getProperty("jdbc.user");
        this.pwd = properties.getProperty("jdbc.pwd");
    }

    /**
     * Takes a prepared statement as a parameter which is being executed in no transactional way upon specified function and returns the result based on
     * what sql statement was executed for. Be aware that AutoCommit property is set to true.
     *
     * @param sql      statement to be executed.
     * @param function all the manipulations with the prepared statement.
     * @param <T>      result upon function's return value.
     * @return result of sql statement execution.
     * @throws IllegalStateException if execution failed.
     */
    public <T> T executeStatement(String sql, Function<PreparedStatement, T> function) {
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            return function.apply(ps);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL statement \"" + sql + "\" couldn't be executed! (SQL error code: " + e.getErrorCode() + ")", e);
            throw new IllegalStateException("SQL statement \"" + sql + "\" failed!");
        }
    }

    /**
     * Takes a function which manipulates with Connection instance.
     * AutoCommit is set to false before execution and there is no need to manually call {@link Connection#commit()}
     * as it is being called after all the operations done in the function.
     *
     * @param function all the manipulations with the prepared statement.
     * @param <T>      result upon function's return value.
     * @return result of sql statement execution.
     * @throws IllegalStateException if execution failed.
     */
    public <T> T executeTransaction(Function<Connection, T> function) {
        try (Connection conn = getConnection()) {
            try {
                conn.setAutoCommit(false);
                T result = function.apply(conn);
                conn.commit();
                return result;
            } catch (SQLException e) {
                conn.rollback();
                logger.log(Level.SEVERE, "Something has gone wrong executing the transaction! (SQL error code: " + e.getErrorCode() + ")", e);
                throw e;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Transaction failed! SQL error code: " + e.getErrorCode(), e);
        }
    }

    /**
     * @return Connection instance which lets to perform operations with the database.
     * See <li>config.properties</li> file to set all the parameters.
     * @throws SQLException if something goes wrong when getting the instance.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pwd);
    }
}
