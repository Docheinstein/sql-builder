package com.docheinstein.sqlbuilder.types;

import com.docheinstein.sqlbuilder.types.base.Type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Int extends Type<Integer> {
    private Integer mLength;

    public Int() {

    }

    public Int(int length) {
        mLength = length;
    }

    @Override
    public String toSql() {
        return "INTEGER" + (mLength != null ? "(" + mLength + ")" : "") ;
    }

    @Override
    public Integer getFromResultSet(ResultSet resultSet, String columnName)
        throws SQLException {
        return resultSet.getInt(columnName);
    }

}
