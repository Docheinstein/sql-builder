package com.docheinstein.sqlbuilder.types;

import com.docheinstein.sqlbuilder.types.base.Type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

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