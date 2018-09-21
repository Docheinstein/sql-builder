package org.docheinstein.sqlbuilder.commons;

/**
 * Type of language used for language specific statements or clause.
 * <p>
 * Can be set via {@link SqlBuilder#setLanguage(SqlLanguage)}.
 */
public enum SqlLanguage {
    MySQL,
    PostgreSQL
}
