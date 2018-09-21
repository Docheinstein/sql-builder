package org.docheinstein.sqlbuilder.exceptions;

import org.docheinstein.sqlbuilder.commons.SqlLanguage;

/**
 * Exception thrown when a SQL language must be known by an entity
 * but it is has not been set via
 * {@link org.docheinstein.sqlbuilder.commons.SqlBuilder#setLanguage(SqlLanguage)}.
 */
public class UnspecifiedSqlLanguageException extends RuntimeException {

    public UnspecifiedSqlLanguageException() {
        super("The SQL language must be set via SqlBuilder.setLanguage() for perform this operation");
    }
}
