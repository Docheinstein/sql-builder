package com.docheinstein.sqlbuilder.types;

import com.docheinstein.sqlbuilder.types.base.Type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

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