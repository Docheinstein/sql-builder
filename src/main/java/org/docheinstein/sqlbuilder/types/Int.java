package org.docheinstein.sqlbuilder.types;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL type that wraps an {@link Integer});
 * translated into INTEGER.
 */
public class Int extends Type<Integer> {

    /** Maximum length of the INTEGER type. */
    // INTEGER(<length>)
    private Integer mLength;

    /**
     * Creates an INTEGER type with unspecified length
     * (the decision is left to the DBMS).
     */
    public Int() {}

    /**
     * Creates an INTEGER type.
     * @param length the length of the INTEGER
     */
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
        // resultSet.getInt() returns 0 even if the value was null;
        // instead we returns a null value if the result was null
        int fromResultSet = resultSet.getInt(columnName);
        return resultSet.wasNull() ? null : fromResultSet;
    }

}
