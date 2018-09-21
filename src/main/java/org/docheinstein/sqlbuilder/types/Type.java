package org.docheinstein.sqlbuilder.types;

import org.docheinstein.sqlbuilder.Sqlable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a SQL type (INTEGER, VARCHAR, ...)
 * @param <T> the underlying java type this SQL type is mapped to
 */
public abstract class Type<T> implements Sqlable {

    /**
     * Retrieves from the result set of a query the object associated with the
     * given column name.
     * @param resultSet the result set from which retrieve the data
     * @param columnName the name of the column to retrieve
     * @return the data associated with the given column name
     * @throws SQLException if the data can't be retrieved
     */
    public abstract T getFromResultSet(ResultSet resultSet, String columnName)
        throws SQLException;
}
