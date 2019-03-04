package org.docheinstein.sqlbuilder.statements.shared;

import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.commons.adt.Pair;
import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.expressions.Expression;
import org.docheinstein.sqlbuilder.models.Column;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.models.Tuple;
import org.docheinstein.sqlbuilder.statements.base.UpdateStatement;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
UPDATE table_name
SET column1 = value1, column2 = value2, ...
WHERE condition;
*/

/**
 * Represents an UPDATE statement.
 */
public class Update implements UpdateStatement {

    /** Table to update. */
    private Table mTable;

    /** List of assignment between columns and values. */
    private List<Pair<String, Object>> mSetPairs = new ArrayList<>();

    /** WHERE expression. */
    private Expression mWhere;

    /**
     * Creates an UPDATE statement for the given table.
     * @param table the table to update
     */
    public Update(Table table) {
        mTable = table;
    }

    /**
     * Adds a SET assignment between a column and its value.
     * @param column the column
     * @param value the value to assign to the given column
     * @param <T> the shared type between column and value
     * @return this statement
     */
    // This is preferred over the generic set() for have type control
    public <T> Update set(Column<T> column, T value) {
        return set(column.getName(), value);
    }

    /**
     * Adds a SET assignment between a column and its value.
     * @param columnName the column name
     * @param value the value to assign to the given column
     * @return this statement
     */
    public Update set(String columnName, Object value) {
        mSetPairs.add(new Pair<>(columnName, value));
        return this;
    }

    /**
     * Adds a SET assignment for each
     * {@link org.docheinstein.sqlbuilder.models.ColumnField}
     * of the tuple between the annotated column and the tuple's value.
     *
     * <p>
     *
     * This method skip the null values of the tuple; for different behaviour
     * see {@link #setFromTuple(Tuple, boolean).
     *
     * @param tuple the tuple from which figure out the SET assignments
     * @return this statement
     *
     * @see #setFromTuple(Tuple, boolean)
     */
    public Update setFromTuple(Tuple tuple) {
        return setFromTuple(tuple, true);
    }

    /**
     * Adds a SET assignment for each
     * {@link org.docheinstein.sqlbuilder.models.ColumnField}
     * of the tuple between the annotated column and the tuple's value.
     * @param tuple the tuple from which figure out the SET assignments
     * @param skipNull whether null values of the variables of the tuple
     *                 have to be ignored.
     *                 i.e. if skipNull is specified then the columns of the
     *                 null values are not affected by the statement, otherwise
     *                 the column of the null values are assigned as 'column = NULL'
     * @return this statement
     */
    public Update setFromTuple(Tuple tuple, boolean skipNull) {

        // Retrieve the column field map
        Map<String, Field> columnFieldMap;

        try {
            columnFieldMap = SqlBuilderInternalUtil.getColumnFields(tuple.getClass());
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Error while trying to access column field of class "
                + tuple.getClass().getName());
        }

        // Scans every annotated column field and for each of those add a
        // SET value between the referred columns and the value.
        // In case the value of the tuple for a column is null, the behaviour
        // is specified by skipNull: if skipNull is true than we should not
        // put the null fields in the statement. In case skipNull is false
        // we put 'ColumnName = NULL' in the statement

        for (Map.Entry<String, Field> columnField : columnFieldMap.entrySet()) {
            String columnName = columnField.getKey();
            // Logger.out("Found column field with name: " + columnName);
            Object tupleValue;

            try {
                tupleValue = columnField.getValue().get(tuple);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error while trying to access instance of class "
                    + tuple.getClass().getSimpleName());
            }

            if (tupleValue != null || !skipNull)
                set(columnName, tupleValue);
        }

        return this;
    }


    /**
     * Sets the WHERE expression of this statement.
     * @param whereExpression the WHERE expression
     * @return this statement
     */
    public Update where(Expression whereExpression) {
        mWhere = whereExpression;
        return this;
    }


    @Override
    public String toSql() {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(mTable.getName());

        // SET
        if (mSetPairs.size() > 0) {
            sql.append(" SET ");


            sql.append(SqlBuilderInternalUtil.getAsCommaList(
                mSetPairs,
                setPair -> setPair.getKey() + " = ?"));
        }

        // WHERE
        sql.append(SqlBuilderInternalUtil.getNamedExpressionAsString(mWhere, "WHERE"));

        String sqlStr = sql.toString();

        SqlBuilderLogger.out("Created [UPDATE] SQL {" + sqlStr + "}");

        return sqlStr;
    }

    @Override
    public List<Object> getBindableObjects() {
        // The bindable objects are the values of the SET assignemnts and
        // the bindable objects of the WHERE

        List<Object> bindables = new ArrayList<>();

        for (Pair<String, Object> setPair : mSetPairs) {
            SqlBuilderLogger.out("Set bindable: " + setPair.getValue());
            bindables.add(setPair.getValue());
        }

        List<Object> whereBindables = mWhere == null ? null : mWhere.getBindableObjects();

        if (whereBindables != null)
            bindables.addAll(whereBindables);

        for (Object b : whereBindables) {
            SqlBuilderLogger.out("Where bindable: " + b);
        }
        for (Object b : bindables) {
            SqlBuilderLogger.out("Bindable: " + b);
        }
        return bindables;
    }
}
