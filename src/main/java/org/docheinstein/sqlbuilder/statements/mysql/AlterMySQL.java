package org.docheinstein.sqlbuilder.statements.mysql;

import org.docheinstein.sqlbuilder.Sqlable;
import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.commons.SqlLanguage;
import org.docheinstein.sqlbuilder.models.Column;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.shared.Alter;

import java.util.ArrayList;
import java.util.List;

/*
 * ALTER TABLE table ADD COLUMN c after b, DROP COLUMN c, DROP COLUMN d;
 */
/**
 * Represents an ALTER TABLE statement that respect PostgreSQL syntax.
 */
public class AlterMySQL extends Alter {

    public enum AddColumnPrecedence implements Sqlable {
        First("FIRST"),
        After("AFTER");

        AddColumnPrecedence(String sql) {
            mSql = sql;
        }

        private String mSql;

        @Override
        public String toSql() {
            return mSql;
        }
    }

    private class AddColumn {
        public Column c;
        public AddColumnPrecedence p;
        public Column c2;

        public AddColumn(Column c, AddColumnPrecedence p, Column c2) {
            this.c = c;
            this.p = p;
            this.c2 = c2;
        }
    }

    private List<AddColumn> mAddColumnsWithPrecedence = new ArrayList<>();


    /**
     * Creates an MyQL ALTER TABLE statement for the given table.
     * <p>
     * Ensures that {@link org.docheinstein.sqlbuilder.commons.SqlBuilder#setLanguage(SqlLanguage)}
     * has been set properly before create this object.
     *
     * @param table the table to alter
     */
    public AlterMySQL(Table table) {
        super(table);

        SqlBuilderInternalUtil.throwIfCurrentLanguageIsNot(SqlLanguage.MySQL);
    }

    /**
     * Adds a column to the list of columns to add to the table,
     * eventually using the specified column precedence.
     * @param c the column to add
     * @param p an optional precedence
     * @param c2 the column the precedence refers to
     * @return this statement
     */
    public Alter add(Column c, AddColumnPrecedence p, Column c2) {
        mAddColumnsWithPrecedence.add(new AddColumn(c, p, c2));
        return this;
    }

    /**
     * Adds a column to the list of columns to add to the table,
     * using the clause FIRST (before).
     * @param c the column to add
     * @param c2 the column the precedence refers to
     * @return this statement
     */
    public Alter addFirst(Column c, Column c2) {
        return add(c, AddColumnPrecedence.First, c2);
    }

    /**
     * Adds a column to the list of columns to add to the table,
     * using the clause AFTER.
     * @param c the column to add
     * @param c2 the column the precedence refers to
     * @return this statement
     */
    public Alter addAfter(Column c, Column c2) {
        return add(c, AddColumnPrecedence.After, c2);
    }

    @Override
    public Alter add(Column c) {
        return add(c, null, null);
    }

    @Override
    public List<Object> getBindableObjects() {
        List<Object> obs = new ArrayList<>();
        for (AddColumn cpc : mAddColumnsWithPrecedence) {
            List cObjs = cpc.c.getBindableObjects();
            if (cObjs != null)
                obs.addAll(cObjs);
        }
        return obs;
    }

    protected String getAddColumnsSection() {
        StringBuilder s = new StringBuilder(
            SqlBuilderInternalUtil.getAsCommaList(
                mAddColumnsWithPrecedence,
                cpc ->
                    "ADD COLUMN " + cpc.c.getColumnDefinition() +
                    // Precedence
                    (cpc.p == null ?
                        "" :
                        " " + cpc.p.toSql() + " " + cpc.c2.getName())
                )
        );

        if (mAddColumnsWithPrecedence.size() > 0 && mDropColumns.size() > 0)
            s.append(", ");

        return s.toString();
    }
}
