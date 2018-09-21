package org.docheinstein.sqlbuilder.statements.base;

import org.docheinstein.sqlbuilder.SqlBindable;

/**
 * Represents a SQL statement (e.g. SELECT, CREATE TABLE, ...)
 * <p>
 * Actually this is nothing but a {@link SqlBindable}.
 */
public interface Statement extends SqlBindable {}
