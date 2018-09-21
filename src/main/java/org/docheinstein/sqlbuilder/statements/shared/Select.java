package org.docheinstein.sqlbuilder.statements.shared;

import org.docheinstein.sqlbuilder.clauses.Join;
import org.docheinstein.sqlbuilder.clauses.OrderBy;
import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.commons.adt.Pair;
import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.expressions.Expression;
import org.docheinstein.sqlbuilder.expressions.Operators;
import org.docheinstein.sqlbuilder.models.*;
import org.docheinstein.sqlbuilder.statements.base.QueryStatement;

import java.util.*;

/*
 * SELECT c1, c2, ...
 * FROM table_name
 * INNER JOIN table_name.c1 = another_table_name.c3
 * WHERE condition
 * GROUP BY c2
 * HAVING condition_2
 * ORDER BY c1
 * LIMIT 0, 1
 */

/**
 * Represents a SELECT FROM statement.
 */
public class Select implements QueryStatement {

    /** Table to select from. */
    private Table mTable;

    /** Columns to select. */
    private List<Column> mColumns = new ArrayList<>();

    /** Columns to use for the ORDER BY clause. */
    private List<Pair<Column, OrderBy>> mOrderBy = new ArrayList<>();

    /** Columns to use for the GROUP BY clause. */
    private List<Column> mGroupBy = new ArrayList<>();

    /** JOIN conditions */
    private List<Join> mJoinColumns = new ArrayList<>();

    /** Values of the LIMIT clause. */
    private Pair<Integer, Integer> mLimit;

    /** WHERE expression. */
    private Expression mWhere = null;

    /** HAVING expression. */
    private Expression mHaving = null;

    /** Whether use the DISTINCT clause. */
    private boolean mDistinct = false;

    /**
     * Creates a SELECT statement for the given columns.
     * @param columns the columns to retrieve
     */
    public Select(String... columns) {
        for (String column : columns)
            mColumns.add(new StringColumn(column));
    }

    /**
     * Creates a SELECT statement for the given columns.
     * @param columns the columns to retrieve
     */
    public Select(Column... columns) {
        mColumns.addAll(Arrays.asList(columns));
    }

    /**
     * Creates a SELECT statement for the given columns.
     * @param columns the columns to retrieve
     */
    public Select(List<Column> columns) {
        mColumns.addAll(columns);
    }

    /**
     * Sets the table from which retrieve the data.
     * @param table the table
     * @return this statement
     */
    public Select from(Table table) {
        mTable = table;
        return this;
    }

    /**
     * Sets the WHERE expression to use.
     * @param whereExpression the WHERE expression
     * @return this statement
     */
    public Select where(Expression whereExpression) {
        mWhere = whereExpression;
        return this;
    }

    /**
     * Adds an ORDER BY column using ASC as order.
     * @param column the column for which add the ORDER BY condition
     * @return this statement
     */
    public Select orderByAsc(Column column) { return orderBy(column, OrderBy.Asc); }

    /**
     * Adds an ORDER BY column using DESC as order.
     * @param column the column for which add the ORDER BY condition
     * @return this statement
     */
    public Select orderByDesc(Column column) { return orderBy(column, OrderBy.Desc); }

    /**
     * Adds an ORDER BY column using the specified order.
     * @param column the column for which add the ORDER BY condition
     * @param orderBy the order (ASC | DESC)
     * @return this statement
     */
    public Select orderBy(Column column, OrderBy orderBy) {
        mOrderBy.add(new Pair<>(column, orderBy));
        return this;
    }

//    public Select orderBy(String column, OrderBy orderBy) {
//        return orderBy(new StringColumn(column), orderBy);
//    }

    /**
     * Adds a GROUP BY column.
     * @param column the column for which add the GROUP BY condition
     * @return this statement
     */
    public Select groupBy(Column column) {
        mGroupBy.add(column);
        return this;
    }

    /**
     * Sets the HAVING expression to use.
     * @param havingExpression the HAVING expression
     * @return this statement
     */
    public Select having(Expression havingExpression) {
        mHaving = havingExpression;
        return this;
    }

    /**
     * Adds an INNER JOIN for the given columns.
     * @param external the first column (typically external)
     * @param internal the second column (typically internal)
     * @param <T> the type of the columns (must be the same)
     * @return this statement
     */
    public <T> Select innerJoin(Column<T> external, Column<T> internal) {
        return join(Join.Type.Inner, external, internal);
    }

    /**
     * Adds a LEFT JOIN for the given columns.
     * @param external the first column (typically external)
     * @param internal the second column (typically internal)
     * @param <T> the type of the columns (must be the same)
     * @return this statement
     */
    public <T> Select leftJoin(Column<T> external, Column<T> internal) {
        return join(Join.Type.Left, external, internal);
    }

    /**
     * Adds a RIGHT JOIN for the given columns.
     * @param external the first column (typically external)
     * @param internal the second column (typically internal)
     * @param <T> the type of the columns (must be the same)
     * @return this statement
     */
    public <T> Select rightJoin(Column<T> external, Column<T> internal) {
        return join(Join.Type.Right, external, internal);
    }

    /**
     * Adds a FULL JOIN for the given columns.
     * @param external the first column (typically external)
     * @param internal the second column (typically internal)
     * @param <T> the type of the columns (must be the same)
     * @return this statement
     */
    public <T> Select fullJoin(Column<T> external, Column<T> internal) {
        return join(Join.Type.Full, external, internal);
    }

    /**
     * Adds a JOIN with the specified type for the given columns.
     * @param type the type of JOIN
     * @param external the first column (typically external)
     * @param internal the second column (typically internal)
     * @param <T> the type of the columns (must be the same)
     * @return this statement
     */
    public <T> Select join( Join.Type type, Column<T> external, Column<T> internal) {
        mJoinColumns.add(new Join<>(type, external, internal));
        return this;
    }

    /**
     * Sets the first value of the LIMIT clause which is the maximum
     * number of rows to return.
     * @param numrows the first value of the LIMIT clause (number of rows)
     * @return this statement
     */
    public Select limit(Integer numrows) {
        return limit(null, numrows);
    }

    /**
     * Sets the values of the LIMIT clause which are the index of the first
     * row to return and the number of rows to return beginning from the specified
     * one.
     * @param from the first value of the LIMIT clause (first row)
     * @param numrows the second value of the LIMIT clause (number of rows)
     * @return this statement
     */
    public Select limit(Integer from, Integer numrows) {
        mLimit = new Pair<>(from, numrows);
        return this;
    }

    /**
     * Sets the DISTINCT clause.
     * @return this statement
     */
    public Select distinct() {
        return distinct(true);
    }

    /**
     * Sets/unsets the DISTINCT clause.
     * @param distinct whether use the DISTINCT clause
     * @return this statement
     */
    public Select distinct(boolean distinct) {
        mDistinct = distinct;
        return this;
    }

    // -------------------------------------------------------------------------
    // ------------------------- SUB QUERY OPERATORS ---------------------------
    // -------------------------------------------------------------------------

    /*
     * SELECT column_name(s)
     * FROM table_name
     * WHERE column_name operator ALL | ANY | SOME
     * (SELECT column_name FROM table_name WHERE condition);
     */

    /**
     * Returns an ALL operator that encapsulates this statement.
     * <p>
     * Can be used for create sub queries.
     * @return an ALL operator that contains this statement
     */
    public Operators.All all() { return new Operators.All(this); }

    /**
     * Returns an ANY operator that encapsulates this statement.
     * <p>
     * Can be used for create sub queries.
     * @return an ANY operator that contains this statement
     */
    public Operators.Any any() { return new Operators.Any(this); }

    /**
     * Returns a SOME operator that encapsulates this statement.
     * <p>
     * Can be used for create sub queries.
     * @return a SOME operator that contains this statement
     */
    public Operators.Some some() { return new Operators.Some(this); }

    /*
     * SELECT column1 FROM t1 WHERE EXISTS (SELECT * FROM t2)
     */
    /**
     * Returns an EXISTS operator that encapsulates this statement.
     * <p>
     * Can be used for create sub queries.
     * @return an EXISTS operator that contains this statement
     */
    public Operators.Exists exists() { return new Operators.Exists(this); }

    /**
     * Returns a NOT EXISTS operator that encapsulates this statement.
     * <p>
     * Can be used for create sub queries.
     * @return a NOT EXISTS operator that contains this statement as left operand
     */
    public Operators.NotExists notExists() { return new Operators.NotExists(this); }

    // -------------------------------------------------------------------------
    // -------------------------- BINARY OPERATORS -----------------------------
    // -------------------------------------------------------------------------

    /*
     * (SELECT a FROM t1 WHERE a=10 AND B=1 ORDER BY a LIMIT 10)
     * EXCEPT | UNION | INTERSECT
     * (SELECT a FROM t2 WHERE a=11 AND B=2 ORDER BY a LIMIT 10);
     */

    /**
     * Returns an EXCEPT operator that links this statement to another one.
     * <p>
     * Can be used for create binary queries.
     * @param s the second statement
     * @return an EXCEPT operator
     */
    public Operators.Except except(Select s) { return new Operators.Except(this, s); }

    /**
     * Returns an UNION operator that links this statement to another one.
     * <p>
     * Can be used for create binary queries.
     * @param s the second statement
     * @return an UNION operator
     */
    public Operators.Union union(Select s) { return new Operators.Union(this, s); }

    /**
     * Returns an INTERSECT operator that links this statement to another one.
     * <p>
     * Can be used for create binary queries.
     * @param s the second statement
     * @return an INTERSECT operator
     */
    public Operators.Intersect intersect(Select s) { return new Operators.Intersect(this, s); }

    @Override
    public String toSql() {
        StringBuilder sql = new StringBuilder("SELECT ");

        // DISTINCT
        if (mDistinct)
            sql.append("DISTINCT ");

        sql.append(SqlBuilderInternalUtil.getAsCommaList(
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
        sql.append(SqlBuilderInternalUtil.getNamedExpressionAsString(mWhere, "WHERE"));

        // GROUP BY
        if (mGroupBy.size() > 0) {
            sql.append(" GROUP BY ");

            sql.append(SqlBuilderInternalUtil.getAsCommaList(
                mGroupBy,
                Column::getTableDotName));
        }
        
        sql.append(SqlBuilderInternalUtil.getNamedExpressionAsString(mHaving, "HAVING"));
        
        // ORDER BY
        if (mOrderBy.size() > 0) {
            sql.append(" ORDER BY ");

            sql.append(SqlBuilderInternalUtil.getAsCommaList(
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

        SqlBuilderLogger.out("Created SELECT SQL {" + sqlStr + "}");

        return sqlStr;
    }

    @Override
    public List<Object> getBindableObjects() {
        return mWhere == null ? null : mWhere.getBindableObjects();
    }

    public List<Column> getColumns() {
        return mColumns;
    }
}
