package com.docheinstein.sqlbuilder.types;

import com.docheinstein.sqlbuilder.types.base.Type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Bool extends Type<Boolean> {

    @Override
    public String toSql() {
        return "TINYINT(1)";
    }

    @Override
    public Boolean getFromResultSet(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getBoolean(columnName);
    }
}
