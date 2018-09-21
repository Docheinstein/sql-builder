package org.docheinstein.sqlbuilder.types;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL type that wraps a character (actually a {@link String});
 * translated into CHAR.
 */
public class Char extends Type<String> {

    /** Maximum length of the CHAR type. */
    // CHAR(<length>)
    private int mLength;

    /**
     * Creates a CHAR type.
     * @param length the length of the CHAR
     */
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
