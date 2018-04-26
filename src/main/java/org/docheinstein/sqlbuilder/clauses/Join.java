package com.docheinstein.sqlbuilder.clauses;

import com.docheinstein.sqlbuilder.Sqlable;
import com.docheinstein.sqlbuilder.models.Column;

public class Join<T> {

    public enum Type implements Sqlable {
        Inner("INNER"),
        Left("LEFT"),
        Right("RIGHT"),
        Full("FULL");

        Type(String sql) {
            mSql = sql;
        }

        private String mSql;

        @Override
        public String toSql() {
            return mSql;
        }

    }

    private Type mType;
    private Column<T> mInternalColumn;
    private Column<T> mExternalColumn;

    public Join(Type type, Column<T> external, Column<T> internal) {
        mType = type;
        mExternalColumn = external;
        mInternalColumn = internal;
    }

    public Type getType() {
        return mType;
    }

    public Column<T> getExternalColumn() {
        return mExternalColumn;
    }

    public Column<T> getInternalColumn() {
        return mInternalColumn;
    }
}
