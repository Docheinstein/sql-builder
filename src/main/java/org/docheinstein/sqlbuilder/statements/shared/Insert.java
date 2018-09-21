package org.docheinstein.sqlbuilder.statements.shared;

import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.base.Put;

import java.util.ArrayList;
import java.util.List;

/*
 * INSERT INTO User VALUES (?, ?, ?)
 */

/**
 * Represents an INSERT INTO statement.
 */
public class Insert extends Put<Insert> {

    /** Whether use the IGNORE clause. */
    private boolean mIgnore;

    /**
     * Creates an INSERT statement for the given table.
     * @param table the table
     */
    public Insert(Table table) {
        super(table);
    }

    /**
     * Sets/unsets the IGNORE clause.
     * @param ignore whether set the IGNORE clause
     * @return this statement
     */
    public Insert ignore(boolean ignore) {
        mIgnore = ignore;
        return this;
    }

    /**
     * Sets the IGNORE clause.
     * @return this statement
     */
    public Insert ignore() {
        return ignore(true);
    }

    @Override
    public String toSql() {
        StringBuilder sql = new StringBuilder("INSERT ");

        if (mIgnore)
            sql.append("IGNORE ");

        sql.append("INTO ");
        sql.append(mTable.getName());
        sql.append(" VALUES ");

        // VALUES
        sql.append(SqlBuilderInternalUtil.getAsCommaList(
            mValuesList,
            valueList ->
                "(" +
                    SqlBuilderInternalUtil.getAsCommaList(
                        valueList,
                        value -> "?") +
                    ")"
        ));

        String sqlStr = sql.toString();

        SqlBuilderLogger.out("Created [INSERT] SQL {" + sqlStr + "}");

        return sqlStr;
    }

    @Override
    public List<Object> getBindableObjects() {
        List<Object> bindables = new ArrayList<>();
        mValuesList.forEach(bindables::addAll);
        return bindables;
    }

    @Override
    public Insert getThis() {
        return this;
    }
}
