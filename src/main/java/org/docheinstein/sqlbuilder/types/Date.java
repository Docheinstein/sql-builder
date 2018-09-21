package org.docheinstein.sqlbuilder.types;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * SQL type that wraps a date (actually a {@link String});
 * translated into DATE.
 */
public class Date extends Type<String> {

    @Override
    public String toSql() {
        return "DATE";
    }

    @Override
    public String getFromResultSet(ResultSet resultSet, String columnName) throws SQLException {
        java.sql.Date d = resultSet.getDate(columnName, Calendar.getInstance());
        return d != null ? d.toString() : null;
    }
}