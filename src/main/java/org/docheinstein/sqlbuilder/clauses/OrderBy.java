package com.docheinstein.sqlbuilder.clauses;

import com.docheinstein.sqlbuilder.Sqlable;

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
