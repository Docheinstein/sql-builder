package org.docheinstein.sqlbuilder.types;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TinyInt extends Type<Integer> {
    private int mLength;

    public TinyInt(int length) {
        mLength = length;
    }

    @Override
    public String toSql() {
        return String.format(
            "TINYINT(%d)",
            mLength
        );
    }

    @Override
    public Integer getFromResultSet(ResultSet resultSet, String columnName)
        throws SQLException {
        return resultSet.getInt(columnName);
    }
}
