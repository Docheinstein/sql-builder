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
        // resultSet.getInt() returns 0 even if the value was null;
        // instead we returns a null value if the result was null
        int fromResultSet = resultSet.getInt(columnName);
        return resultSet.wasNull() ? null : fromResultSet;
    }
}
