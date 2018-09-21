package org.docheinstein.sqlbuilder.statements.base;

import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a generic update statement (DELETE, UPDATE, INSERT, REPLACE)
 * for modify data on the database
 */
public interface UpdateStatement extends Statement {

    /**
     * Executes this statement over the specified connection.
     * @param connection the connection
     * @return the number of affected rows
     * @throws SQLException if the statement execution fails
     */
    default int exec(Connection connection) throws SQLException {
        return exec(connection, false);
    }

    /**
     * Executes this statement over the specified connection.
     * @param connection the connection
     * @param returnLastInsertedId true for return the key of the affected row
     *                             false for return the number of affected rows
     * @return the number of affected rows or the key of the affected row
     * @throws SQLException if the statement execution fails
     */
    default int exec(Connection connection, boolean returnLastInsertedId) throws SQLException {
        if (connection == null)
            return 0;

        return execInternal(
            SqlBuilderInternalUtil.boundStatement(
                SqlBuilderInternalUtil.getStatement(
                    connection, toSql(), returnLastInsertedId), getBindableObjects()
            ), returnLastInsertedId
        );
    }


    /**
     * Executes this statement over the specified connection using a previously
     * cached {@link java.sql.PreparedStatement} if it exists or creating a new
     * one if it doesn't.
     * @param connection the connection
     * @param identifier the cache identifier of this statement
     * @return the number of affected rows
     * @throws SQLException if the statement execution fails
     */
    default int execCache(Connection connection, int identifier) throws SQLException {
        return execCache(connection, identifier, false);
    }

    /**
     * Executes this statement over the specified connection using a previously
     * cached {@link java.sql.PreparedStatement} if it exists or creating a new
     * one if it doesn't.
     * @param connection the connection
     * @param identifier the cache identifier of this statement
     * @param returnLastInsertedId true for return the key of the affected row
     *                             false for return the number of affected rows
     * @return the number of affected rows or the key of the affected row
     * @throws SQLException if the statement execution fails
     */
    default int execCache(Connection connection, int identifier,
                         boolean returnLastInsertedId) throws SQLException {
        if (connection == null)
            return 0;

        return execInternal(
            SqlBuilderInternalUtil.getBoundStatement(
                    connection, this, identifier, returnLastInsertedId
            ), returnLastInsertedId);
    }

    /**
     * Executes the given {@link PreparedStatement}.
     * @param preparedStatement the statement
     * @param returnLastInsertedId true for return the key of the affected row
     *                             false for return the number of affected rows
     * @return the number of affected rows or the key of the affected row
     * @throws SQLException if the statement execution fails
     */
    default int execInternal(PreparedStatement preparedStatement,
                             boolean returnLastInsertedId) throws SQLException {
        int execRes = preparedStatement.executeUpdate();

        if (!returnLastInsertedId)
            return execRes;

        ResultSet keys = preparedStatement.getGeneratedKeys();
        keys.next();

        if (keys.isAfterLast())
            return execRes;

        return keys.getInt(1);
    }
}