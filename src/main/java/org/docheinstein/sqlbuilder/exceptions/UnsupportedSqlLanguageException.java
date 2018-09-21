package org.docheinstein.sqlbuilder.exceptions;

import org.docheinstein.sqlbuilder.commons.SqlLanguage;

/**
 * Exception thrown when a specific language statement requires a SQL language
 * different from the one specified via
 * {@link org.docheinstein.sqlbuilder.commons.SqlBuilder#setLanguage(SqlLanguage)}.
 */
public class UnsupportedSqlLanguageException extends RuntimeException {

    public UnsupportedSqlLanguageException() {
        super("The required operation can't be performed with the specified SQL language");
    }
}