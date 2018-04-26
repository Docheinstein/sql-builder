package com.docheinstein.sqlbuilder.types.base;

import com.docheinstein.sqlbuilder.Sqlable;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Type<T> implements Sqlable {
    public abstract T getFromResultSet(ResultSet resultSet, String columnName)
        throws SQLException;
}
