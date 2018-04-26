package com.docheinstein.sqlbuilder.types;

import com.docheinstein.sqlbuilder.types.base.Type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Char extends Type<String> {
    private int mLength;

    public Char(int length) {
        mLength = length;
    }

    @Override
    public String toSql() {
        return String.format(
            "CHAR(%d)",
            mLength
        );
    }

    @Override
    public String getFromResultSet(ResultSet resultSet, String columnName)
        throws SQLException {
        return resultSet.getString(columnName);
    }
}
