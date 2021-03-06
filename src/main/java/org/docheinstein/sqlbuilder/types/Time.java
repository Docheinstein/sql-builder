package org.docheinstein.sqlbuilder.types;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * SQL type that wraps a time (actually a {@link String});
 * translated into TIME.
 */
public class Time extends Type<String> {

    @Override
    public String toSql() {
        return "TIME";
    }

    @Override
    public String getFromResultSet(ResultSet resultSet, String columnName) throws SQLException {
        java.sql.Time t = resultSet.getTime(columnName, Calendar.getInstance());
        return t != null ? t.toString() : null;
    }
}