package org.docheinstein.sqlbuilder.statements.shared;

import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.expressions.Expression;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.base.UpdateStatement;

import java.util.List;

/*
 * DELETE FROM table_name
 * WHERE condition;
 */

/**
 * Represents a DELETE FROM statement.
 */
public class Delete implements UpdateStatement {

    /** Table from which delete. */
    private Table mTable;

    /** Where expression of this statement. */
    private Expression mWhere;

    /**
     * Creates a DELETE FROM statement for the given table.
     * @param table the table
     */
    public Delete(Table table) {
        mTable = table;
    }

    /**
     * Sets the where expression to use for this DELETE FROM statement.
     * @param whereExpression the where condition
     * @return this statement
     */
    public Delete where(Expression whereExpression) {
        mWhere = whereExpression;
        return this;
    }

    @Override
    public String toSql() {
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(mTable.getName());

        // WHERE
        sql.append(SqlBuilderInternalUtil.getNamedExpressionAsString(mWhere, "WHERE"));

        String sqlStr = sql.toString();

        SqlBuilderLogger.out("Created [DELETE TABLE] SQL {" + sqlStr + "}");
        return sqlStr;
    }

    @Override
    public List<Object> getBindableObjects() {
        return mWhere == null ? null : mWhere.getBindableObjects();
    }
}
