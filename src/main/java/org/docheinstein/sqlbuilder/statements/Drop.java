package com.docheinstein.sqlbuilder.statements;

import com.docheinstein.sqlbuilder.common.SqlBuilderLogger;
import com.docheinstein.sqlbuilder.models.Table;
import com.docheinstein.sqlbuilder.statements.base.UpdateStatement;

import java.util.List;

public class Drop extends UpdateStatement {
    private Table mTable;

    public Drop(Table table) {
        mTable = table;
    }

    @Override
    public String toSql() {
        StringBuilder sql = new StringBuilder("DROP TABLE ");
        sql.append(mTable.getName());

        String sqlStr = sql.toString();
        SqlBuilderLogger.out("Created SQL: " + sqlStr);
        return sqlStr;
    }

    @Override
    public List<Object> getBindableObjects() {
        return null;
    }
}
