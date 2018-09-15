package org.docheinstein.sqlbuilder.exceptions;

public class UnsupportedSqlLanguage extends RuntimeException {

    public UnsupportedSqlLanguage() {
        super("The required operation can't be performed with the specified SQL language");
    }
}