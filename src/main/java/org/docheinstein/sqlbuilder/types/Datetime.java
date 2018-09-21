package org.docheinstein.sqlbuilder.types;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * SQL type that wraps a datetime (actually a {@link String});
 * translated into DATETIME.
 */
public class Datetime extends Type<String> {

    @Override
    public String toSql() {
        return "DATETIME";
    }

    @Override
    public String getFromResultSet(ResultSet resultSet, String columnName) throws SQLException {
        java.sql.Timestamp t = resultSet.getTimestamp(columnName, Calendar.getInstance());
        return t != null ? t.toString() : null;
    }
}