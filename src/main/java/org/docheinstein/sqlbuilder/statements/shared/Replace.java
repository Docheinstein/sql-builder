package org.docheinstein.sqlbuilder.statements.shared;

import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.base.Put;

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
