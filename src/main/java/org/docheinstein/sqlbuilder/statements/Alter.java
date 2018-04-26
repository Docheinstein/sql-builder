package org.docheinstein.sqlbuilder.statements;

import org.docheinstein.sqlbuilder.common.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.common.SqlBuilderUtil;
import org.docheinstein.sqlbuilder.models.Column;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.base.UpdateStatement;

import java.util.ArrayList;
import java.util.List;

public class Alter extends UpdateStatement {
    private Table mTable;
    private List<Column> mAddColumns = new ArrayList<>();
    private List<Column> mDropColumns = new ArrayList<>();

    public Alter(Table table) {
        mTable = table;
    }

    @Override
    public String toSql() {
        StringBuilder sql = new StringBuilder("ALTER TABLE ");
        sql.append(mTable.getName());
        sql.append(" ");

        sql.append(SqlBuilderUtil.getAsCommaList(
            mAddColumns, c ->
                "ADD COLUMN " + c.getColumnDefinition())
        );

        if (mAddColumns.size() > 0 && mDropColumns.size() > 0)
            sql.append(", ");

        sql.append(SqlBuilderUtil.getAsCommaList(
            mDropColumns, c ->
                "DROP COLUMN " + c.getColumnDefinition())
        );

        String sqlStr = sql.toString();
        SqlBuilderLogger.out("Created SQL: " + sqlStr);
        return sqlStr;
    }

    public Alter add(Column c) {
        mAddColumns.add(c);
        return this;
    }

    public Alter drop(Column c) {
        mDropColumns.add(c);
        return this;
    }

    @Override
    public List<Object> getBindableObjects() {
        return null;
    }
}
