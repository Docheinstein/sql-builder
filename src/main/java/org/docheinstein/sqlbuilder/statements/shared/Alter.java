package org.docheinstein.sqlbuilder.statements.shared;

import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.models.Column;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.base.UpdateStatement;

import java.util.ArrayList;
import java.util.List;

/*
 * ALTER TABLE table DROP COLUMN c, DROP COLUMN d;
 */
/**
 * Represents an ALTER TABLE statement.
 */
public class Alter implements UpdateStatement {

    /** Table to alter. */
    private Table mTable;

    /** List of columns to add to the table. */
    protected List<Column> mAddColumns = new ArrayList<>();

    /** List of columns to drop from the table. */
    protected List<Column> mDropColumns = new ArrayList<>();

    /**
     * Creates an ALTER TABLE statement for the given table.
     * @param table the table to alter
     */
    public Alter(Table table) {
        mTable = table;
    }

    /**
     * Adds a column to the list of columns to add to the table.
     * @param c the column to add
     * @return this statement
     */
    public Alter add(Column c) {
        mAddColumns.add(c);
        return this;
    }

    /**
     * Adds a column to the list of columns to drop from the table.
     * @param c the column to add
     * @return this statement
     */
    public Alter drop(Column c) {
        mDropColumns.add(c);
        return this;
    }

    @Override
    public String toSql() {
        StringBuilder sql = new StringBuilder("ALTER TABLE ");
        sql.append(mTable.getName());
        sql.append(" ");

        sql.append(getAddColumnsSection());

        sql.append(SqlBuilderInternalUtil.getAsCommaList(
            mDropColumns,
            c -> "DROP COLUMN " + c.getName())
        );

        String sqlStr = sql.toString();

        SqlBuilderLogger.out("Created [ALTER] SQL {" + sqlStr + "}");

        return sqlStr;
    }

    @Override
    public List<Object> getBindableObjects() {
        List<Object> obs = new ArrayList<>();
        for (Column c : mAddColumns) {
            List cObjs = c.getBindableObjects();
            if (cObjs != null)
                obs.addAll(cObjs);
        }
        return obs;
    }

    protected String getAddColumnsSection() {
        StringBuilder s = new StringBuilder(
            SqlBuilderInternalUtil.getAsCommaList(
                mAddColumns,
                c -> "ADD COLUMN " + c.getColumnDefinition())
            );

        if (mAddColumns.size() > 0 && mDropColumns.size() > 0)
            s.append(", ");

        return s.toString();
    }
}
