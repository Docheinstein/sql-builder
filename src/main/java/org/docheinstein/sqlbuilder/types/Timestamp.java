package org.docheinstein.sqlbuilder.types;

import org.docheinstein.sqlbuilder.types.base.Type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class Timestamp extends Type<String> {

    @Override
    public String toSql() {
        return "TIMESTAMP";
    }

    @Override
    public String getFromResultSet(ResultSet resultSet, String columnName) throws SQLException {
        java.sql.Timestamp t = resultSet.getTimestamp(columnName, Calendar.getInstance());
        return t != null ? t.toString() : null;
    }
}
