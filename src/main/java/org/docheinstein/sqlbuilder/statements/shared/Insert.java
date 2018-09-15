package org.docheinstein.sqlbuilder.statements.shared;

import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.commons.SqlBuilderUtil;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.base.Put;

import java.util.ArrayList;
import java.util.List;

/*
INSERT INTO User VALUES (?, ?, ?)
*/

public class Insert extends Put<Insert> {

    private boolean mIgnore;

    public Insert(Table table) {
        super(table);
    }

    public Insert ignore(boolean ignore) {
        mIgnore = ignore;
        return this;
    }

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
        sql.append(SqlBuilderUtil.getAsCommaList(
            mValuesList,
            valueList ->
                "(" +
                    SqlBuilderUtil.getAsCommaList(
                        valueList,
                        value -> /* value == null ? "NULL" : */ "?") +
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
    public Insert getThis() {
        return this;
    }
}
