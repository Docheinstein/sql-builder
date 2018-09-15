package org.docheinstein.sqlbuilder.statements.base;

import org.docheinstein.sqlbuilder.Sqlable;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class SingleShotStatement implements Sqlable {

    public boolean exec(Connection connection) throws SQLException {
        if (connection == null)
            return false;

        return connection.createStatement().execute(toSql());
    }
}
