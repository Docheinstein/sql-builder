package org.docheinstein.sqlbuilder.types;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL type that wraps an {@link Integer};
 * translated into TINYINT.
 */
public class TinyInt extends Type<Integer> {

    /** Maximum length of the TINYINT type. */
    // VARCHAR(<length>)
    private int mLength;

    /**
     * Creates a TINYINT type.
     * @param length the length of the TINYINT
     */
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
