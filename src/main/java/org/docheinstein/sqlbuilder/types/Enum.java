package org.docheinstein.sqlbuilder.types;

import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Enum extends Type<String> {
    private String[] mValues;

    public Enum(String... values) {
        mValues = values;
    }

    @Override
    public String getFromResultSet(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getString(columnName);
    }

    @Override
    public String toSql() {
        return
            "ENUM (" +
            SqlBuilderInternalUtil.getAsCommaList(Arrays.asList(mValues), s -> "'" + s + "'")
            + ")";
    }
}
