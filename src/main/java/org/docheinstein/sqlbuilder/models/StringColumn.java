package org.docheinstein.sqlbuilder.models;

public class StringColumn<T> extends Column<T> {
    public StringColumn(String columnName) {
        super((String) null, columnName, null);
    }
}
