package org.docheinstein.sqlbuilder.statements;

import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.base.UpdateStatement;

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
