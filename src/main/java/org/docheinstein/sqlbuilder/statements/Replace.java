package com.docheinstein.sqlbuilder.statements;

import com.docheinstein.sqlbuilder.common.SqlBuilderLogger;
import com.docheinstein.sqlbuilder.common.SqlBuilderUtil;
import com.docheinstein.sqlbuilder.models.Table;
import com.docheinstein.sqlbuilder.statements.base.Put;

import java.util.ArrayList;
import java.util.List;

/*
REPLACE INTO User VALUES (?, ?, ?)
*/

public class Replace extends Put<Replace> {

    public Replace(Table table) {
        super(table);
    }

    @Override
    public String toSql() {
        StringBuilder sql = new StringBuilder("REPLACE INTO ");
        sql.append(mTable.getName());
        sql.append(" VALUES ");

        // VALUES
        sql.append(SqlBuilderUtil.getAsCommaList(
            mValuesList,
            valueList ->
                "(" +
                    SqlBuilderUtil.getAsCommaList(
                        valueList,
                        value -> "?") +
                    ")"
        ));

        String sqlStr = sql.toString();

        SqlBuilderLogger.out("Created SQL: " + sqlStr);

        return sqlStr;
    }

    @Override
    public List<Object> getBindableObjects() {
        List<Object> bindables = new ArrayList<>();
        mValuesList.forEach(bindables::addAll);
        return bindables;
    }

    @Override
    public Replace getThis() {
        return this;
    }
}
