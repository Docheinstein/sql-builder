package org.docheinstein.sqlbuilder.statements.shared;

import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.expressions.Expression;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.base.UpdateStatement;

import java.util.List;

/*
DELETE FROM table_name
WHERE condition;
*/

public class Delete extends UpdateStatement {
    private Table mTable;
    private Expression mWhere;

    public Delete(Table table) {
        mTable = table;
    }

    public Delete where(Expression whereExpression) {
        mWhere = whereExpression;
        return this;
    }

    @Override
    public String toSql() {
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(mTable.getName());

        // WHERE
        sql.append(SqlBuilderInternalUtil.getExpressionCondition(mWhere, "WHERE"));

        String sqlStr = sql.toString();

        SqlBuilderLogger.out("Created [DELETE TABLE] SQL {" + sqlStr + "}");
        return sqlStr;
    }

    @Override
    public List<Object> getBindableObjects() {
        return mWhere == null ? null : mWhere.getBindableObjects();
    }
}
