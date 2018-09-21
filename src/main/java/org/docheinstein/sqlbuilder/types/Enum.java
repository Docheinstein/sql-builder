package org.docheinstein.sqlbuilder.types;

import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * SQL type that wraps an enumeration (actually a {@link String});
 * translated into ENUM.
 */
public class Enum extends Type<String> {

    /** Allowed values of the enumeration. */
    private String[] mValues;

    /**
     * Creates an ENUM type for the given allowed values
     * @param values the enum's values
     */
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
            SqlBuilderInternalUtil.getAsCommaList(
                Arrays.asList(mValues), s -> "'" + s + "'"
            )
            + ")";
    }
}
