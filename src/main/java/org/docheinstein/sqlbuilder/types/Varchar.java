package org.docheinstein.sqlbuilder.types;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL type that wraps a {@link String};
 * translated into VARCHAR.
 */
public class Varchar extends Type<String> {

    /** Maximum length of the VARCHAR type. */
    // VARCHAR(<length>)
    private int mLength;

    /**
     * Creates a VARCHAR type.
     * @param length the length of the VARCHAR
     */
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
