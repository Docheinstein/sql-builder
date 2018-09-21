package org.docheinstein.sqlbuilder.statements.base;

import org.docheinstein.sqlbuilder.Sqlable;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Represents a statement which is not involved in the cache mechanism of
 * this library.
 * <p>
 * Typically the implementation of this entity are statements which are executed
 * just one time per application execution (like {@link CreateTrigger}).
 */
public interface SingleShotStatement extends Sqlable {

    /**
     * Executes this statement over the specified connection
     * @param connection the connection
     * @return whether the statement has been executed successfully
     * @throws SQLException if the statement execution fails
     */
    default boolean exec(Connection connection) throws SQLException {
        if (connection == null)
            return false;

        return connection.createStatement().execute(toSql());
    }
}
