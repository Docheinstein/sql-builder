package org.docheinstein.sqlbuilder.clauses;

import org.docheinstein.sqlbuilder.Sqlable;

/**
 * Represents the ORDER BY clause in a SELECT statement.
 */
public enum OrderBy implements Sqlable {
    Asc("ASC"),
    Desc("DESC");

    OrderBy(String sql) {
        mSql = sql;
    }

    private String mSql;

    @Override
    public String toSql() {
        return mSql;
    }
}
