package org.docheinstein.sqlbuilder.types;

import java.sql.ResultSet;
import java.sql.SQLException;


public class Varchar extends Type<String> {
    private int mLength;

    public Varchar(int length) {
        mLength = length;
    }

    @Override
    public String toSql() {
        return String.format(
            "VARCHAR(%d)",
            mLength
        );
    }

    @Override
    public String getFromResultSet(ResultSet resultSet, String columnName)
        throws SQLException {
        return resultSet.getString(columnName);
    }
}
