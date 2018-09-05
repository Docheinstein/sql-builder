package org.docheinstein.sqlbuilder.statements.base;

import org.docheinstein.sqlbuilder.commons.SqlBuilderUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class UpdateStatement implements Statement {
    public int exec(Connection connection) throws SQLException {
        return exec(connection, false);
    }

    public int exec(Connection connection, boolean returnLastInsertedId) throws SQLException {
        if (connection == null)
            return 0;

        return execInternal(
            SqlBuilderUtil.getBoundStatement(
                SqlBuilderUtil.getCachedStatementOrCreate(
                    connection, toSql(), returnLastInsertedId), getBindableObjects()
            ), returnLastInsertedId
        );
    }

    public int execCache(Connection connection, int identifier) throws SQLException {
        return execCache(connection, identifier, false);
    }

    public int execCache(Connection connection, int identifier, boolean returnLastInsertedId) throws SQLException {
        if (connection == null)
            return 0;

        return execInternal(
            SqlBuilderUtil.getBoundCachedStatementOrCreateOrRegenerate(
                    connection, this, identifier, returnLastInsertedId
            ), returnLastInsertedId);
    }

    private int execInternal(PreparedStatement preparedStatement,
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