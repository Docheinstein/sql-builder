package org.docheinstein.sqlbuilder.models;

/**
 * Represents a column which must be treated as a simple string.
 * <p>
 * This is nothing more then an hack for use string column for special cases
 * such as 'SELECT * FROM TABLE' (in this case the string column is '*').
 */
public class StringColumn extends Column<Void> {

    /**
     * Creates a string column with the given column name with no type
     * and with no table associated.
     * @param columnName the column name
     */
    public StringColumn(String columnName) {
        super((String) null, columnName, null);
    }
}
