package org.docheinstein.sqlbuilder.exceptions;

public class UnspecifiedSqlLanguage extends RuntimeException {

    public UnspecifiedSqlLanguage() {
        super("The SQL language must be set via SqlBuilder.setLanguage() for perform this operation");
    }
}
