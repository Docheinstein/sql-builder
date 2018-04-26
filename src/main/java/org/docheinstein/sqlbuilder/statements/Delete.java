package com.docheinstein.sqlbuilder.statements;

import com.docheinstein.sqlbuilder.common.SqlBuilderLogger;
import com.docheinstein.sqlbuilder.common.SqlBuilderUtil;
import com.docheinstein.sqlbuilder.expressions.Expression;
import com.docheinstein.sqlbuilder.models.Table;
import com.docheinstein.sqlbuilder.statements.base.UpdateStatement;

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
        sql.append(SqlBuilderUtil.getExpressionCondition(mWhere, "WHERE"));

        String sqlStr = sql.toString();

        SqlBuilderLogger.out("Created SQL: " + sqlStr);

        return sqlStr;
    }

    @Override
    public List<Object> getBindableObjects() {
        return mWhere == null ? null : mWhere.getBindableObjects();
    }
}
