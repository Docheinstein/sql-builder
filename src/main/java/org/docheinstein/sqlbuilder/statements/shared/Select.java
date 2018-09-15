package org.docheinstein.sqlbuilder.statements.shared;

import org.docheinstein.sqlbuilder.clauses.Join;
import org.docheinstein.sqlbuilder.clauses.OrderBy;
import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.commons.adt.Pair;
import org.docheinstein.sqlbuilder.commons.SqlBuilderUtil;
import org.docheinstein.sqlbuilder.expressions.Expression;
import org.docheinstein.sqlbuilder.expressions.Operators;
import org.docheinstein.sqlbuilder.models.*;
import org.docheinstein.sqlbuilder.statements.base.QueryStatement;

import java.util.*;

/*
SELECT column1, column2, ...
FROM table_name
WHERE condition;
*/

public class Select extends QueryStatement {

    private List<Pair<Column, OrderBy>> mOrderBy = new ArrayList<>();
    private List<Column> mColumns = new ArrayList<>();
    private List<Column> mGroupBy = new ArrayList<>();
    private List<Join> mJoinColumns = new ArrayList<>();
    private Pair<Integer, Integer> mLimit;

    private Table mTable;

    private Expression mWhere = null;
    private Expression mHaving = null;

    public Select(String... columns) {
        for (String column : columns)
            mColumns.add(new StringColumn(column));
    }

    public Select(Column... columns) {
        mColumns.addAll(Arrays.asList(columns));
    }

    public Select(List<Column> columns) {
        mColumns.addAll(columns);
    }

    // FROM

    public Select from(Table table) {
        mTable = table;
        return this;
    }

    // WHERE

    public Select where(Expression whereExpression) {
        mWhere = whereExpression;
        return this;
    }

    // ORDER BY

    public Select orderByAsc(Column column) { return orderBy(column, OrderBy.Asc); }
    public Select orderByDesc(Column column) { return orderBy(column, OrderBy.Desc); }

    public Select orderBy(Column column, OrderBy orderBy) {
        mOrderBy.add(new Pair<>(column, orderBy));
        return this;
    }

//    public Select orderBy(String column, OrderBy orderBy) {
//        return orderBy(new StringColumn(column), orderBy);
//    }

    // GROUP BY

    public Select groupBy(Column column) {
        mGroupBy.add(column);
        return this;
    }

    // HAVING

    public Select having(Expression havingExpression) {
        mHaving = havingExpression;
        return this;
    }

    // JOIN

    public <T> Select innerJoin(Column<T> external, Column<T> internal) {
        return join(Join.Type.Inner, external, internal);
    }

    public <T> Select leftJoin(Column<T> external, Column<T> internal) {
        return join(Join.Type.Left, external, internal);
    }

    public <T> Select rightJoin(Column<T> external, Column<T> internal) {
        return join(Join.Type.Right, external, internal);
    }

    public <T> Select fullJoin(Column<T> external, Column<T> internal) {
        return join(Join.Type.Full, external, internal);
    }

    public <T> Select join( Join.Type type, Column<T> external, Column<T> internal) {
        mJoinColumns.add(new Join<>(type, external, internal));
        return this;
    }

    // Limit

    public Select limit(Integer numrows) {
        return limit(null, numrows);
    }

    public Select limit(Integer from, Integer numrows) {
        mLimit = new Pair<>(from, numrows);
        return this;
    }


    @Override
    public String toSql() {
        // return toSql(true);
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(SqlBuilderUtil.getAsCommaList(
            mColumns,
            Column::getTableDotName
        ));
        sql.append(" FROM ");
        sql.append(mTable.getName());

        mJoinColumns.forEach(join -> {
            sql.append(" ");
            sql.append(join.getType().toSql());
            sql.append(" JOIN ");
            sql.append(join.getExternalColumn().getTable());
            sql.append(" ON ");
            sql.append(join.getExternalColumn().getTableDotName());
            sql.append(" = ");
            sql.append(join.getInternalColumn().getTableDotName());
        });

        // WHERE
        sql.append(SqlBuilderUtil.getExpressionCondition(mWhere, "WHERE"));

        // GROUP BY
        if (mGroupBy.size() > 0) {
            sql.append(" GROUP BY ");

            sql.append(SqlBuilderUtil.getAsCommaList(
                mGroupBy,
                Column::getTableDotName));
        }
        
        sql.append(SqlBuilderUtil.getExpressionCondition(mHaving, "HAVING"));
        
        // ORDER BY
        if (mOrderBy.size() > 0) {
            sql.append(" ORDER BY ");

            sql.append(SqlBuilderUtil.getAsCommaList(
                mOrderBy,
                o -> o.getKey().getTableDotName() + " " + o.getValue().toSql()));
        }

        // LIMIT
        if (mLimit != null) {
            Integer from = mLimit.getKey();
            Integer numrows = mLimit.getValue();

            if (from != null || numrows != null) {
                from = from != null ? from : 0;
                numrows = numrows != null ? numrows : Integer.MAX_VALUE;

                sql.append(" LIMIT ");
                sql.append(from);
                sql.append(", ");
                sql.append(numrows);
            }
        }

        String sqlStr = sql.toString();

        SqlBuilderLogger.out("Created SQL: " + sqlStr);

        return sqlStr;
    }

    @Override
    public List<Object> getBindableObjects() {
        return mWhere == null ? null : mWhere.getBindableObjects();
    }

    @Override
    protected List<Column> getFilteredColumns() {
        return mColumns;
    }

    // ============================
    // === SUB QUERY OPERATORS ====
    // ============================

    public Operators.All all() { return new Operators.All(this); }
    public Operators.Any any() { return new Operators.Any(this); }
    public Operators.Some some() { return new Operators.Some(this); }

    public Operators.Exists exists() { return new Operators.Exists(this); }
    public Operators.NotExists notExists() { return new Operators.NotExists(this); }

    // ==================
    // ===== BINARY =====
    // ==================

    public Operators.Except except(Select s) { return new Operators.Except(this, s); }
    public Operators.Union union(Select s) { return new Operators.Union(this, s); }
    public Operators.Intersect intersect(Select s) { return new Operators.Intersect(this, s); }
}
