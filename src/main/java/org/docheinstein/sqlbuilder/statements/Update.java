package org.docheinstein.sqlbuilder.statements;

import org.docheinstein.sqlbuilder.common.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.common.Pair;
import org.docheinstein.sqlbuilder.common.SqlBuilderUtil;
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

public class Update extends UpdateStatement {
    private Table mTable;
    private List<Pair<String, Object>> mSetPairs = new ArrayList<>();
    private Expression mWhere;

    public Update(Table table) {
        mTable = table;
    }


    public Update setsFromTuple(Tuple tuple) {
        return setsFromTuple(tuple, true);
    }

    public Update setsFromTuple(Tuple tuple, boolean skipNull) {
        // Retrieve the column field map

        Map<String, Field> columnFieldMap;

        try {
            columnFieldMap = SqlBuilderUtil.getCachedColumnFieldOrCreateAndCache(tuple.getClass());
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

    public Update set(String columnName, Object value) {
        mSetPairs.add(new Pair<>(columnName, value));
        return this;
    }

    // Prefer this instead of the generic set() for have type control
    public <T> Update set(Column<T> column, T value) {
        return set(column.getName(), value);
    }

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


            sql.append(SqlBuilderUtil.getAsCommaList(
                mSetPairs, setPair ->
                    setPair.getKey() +
                        " = ?"));
        }

        // WHERE
        sql.append(SqlBuilderUtil.getExpressionCondition(mWhere, "WHERE"));

        String sqlStr = sql.toString();

        SqlBuilderLogger.out("Created SQL: " + sqlStr);

        return sqlStr;
    }

    @Override
    public List<Object> getBindableObjects() {
        List<Object> bindables = new ArrayList<>();

        for (Pair<String, Object> setPair : mSetPairs)
            bindables.add(setPair.getValue());

        List<Object> whereBindables = mWhere == null ? null : mWhere.getBindableObjects();

        if (whereBindables != null)
            bindables.addAll(whereBindables);

        return bindables;
    }
}
