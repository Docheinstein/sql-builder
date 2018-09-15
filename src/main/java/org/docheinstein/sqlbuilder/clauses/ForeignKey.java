package org.docheinstein.sqlbuilder.clauses;

import org.docheinstein.sqlbuilder.Sqlable;
import org.docheinstein.sqlbuilder.models.Column;

public class ForeignKey<T> {
    public enum ReferenceOption implements Sqlable {
        Restrict("RESTRICT"),
        Cascade("CASCADE"),
        SetNull("SET NULL"),
        NoAction("NO ACTION"),
        SetDefault("SET DEFAULT")
        ;

        ReferenceOption(String sql) {
            mSql = sql;
        }

        private String mSql;

        @Override
        public String toSql() {
            return mSql;
        }
    }

    private Column<T> mInternalColumn;
    private Column<T> mExternalColumn;
    private ReferenceOption mOnDelete;
    private ReferenceOption mOnUpdate;

    public ForeignKey(Column<T> internalCol, Column<T> externalCol,
                      ReferenceOption onDelete, ReferenceOption onUpdate) {
        mInternalColumn = internalCol;
        mExternalColumn = externalCol;
        mOnDelete = onDelete;
        mOnUpdate = onUpdate;
    }

    public ForeignKey(Column<T> internalCol, Column<T> externalCol) {
        this(internalCol, externalCol, null, null);
    }

    public ForeignKey onDelete(ReferenceOption what) {
        mOnDelete = what;
        return this;
    }

    public ForeignKey onUpdate(ReferenceOption what) {
        mOnUpdate = what;
        return this;
    }

    public Column<T> getInternalColumn() {
        return mInternalColumn;
    }

    public Column<T> getExternalColumn() {
        return mExternalColumn;
    }

    public ReferenceOption getOnDeleteOption() {
        return mOnDelete;
    }

    public ReferenceOption getOnUpdateOption() {
        return mOnUpdate;
    }
}
