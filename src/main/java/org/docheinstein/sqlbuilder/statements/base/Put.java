package com.docheinstein.sqlbuilder.statements.base;

import com.docheinstein.sqlbuilder.common.SqlBuilderUtil;
import com.docheinstein.sqlbuilder.models.Column;
import com.docheinstein.sqlbuilder.models.Table;
import com.docheinstein.sqlbuilder.models.Tuple;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class Put<T extends Put<T>> extends UpdateStatement {
    protected Table mTable;
    protected List<List<Object>> mValuesList = new ArrayList<>();

    public Put(Table table) {
        mTable = table;
    }

    public T values(List<Object> values) {
        mValuesList.add(values);
        return getThis();
    }

    public T values(Object... values) {
        return values(Arrays.asList(values));
    }

    public T valuesFromTuples(Tuple... tuples) {
        // tuples must be of the same type obviously

        if (tuples == null || tuples.length <= 0)
            return getThis();

        // Retrieve the column field map

        Map<String, Field> columnFieldMap;

        try {
            columnFieldMap = SqlBuilderUtil.getCachedColumnFieldOrCreateAndCache(tuples[0].getClass());
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while trying to access column field of class "
                + tuples[0].getClass().getName() + ". (The class must have a default constructor)");
        }


        for (Tuple tuple : tuples) {
            List<Column> tableColumns = mTable.getColumns();
            List<Object> insertValues = new ArrayList<>();

            // Scans every columns and for each of those retrieve the value
            // from the tuple
            for (Column column : tableColumns) {
                Field field = columnFieldMap.get(column.getName());
                if (field == null)
                    throw new RuntimeException("The tuple of class " + tuple.getClass().getSimpleName() +
                        " doesn't have an annotation for column " + column.getName() + " of the table " + mTable.getName());

                Object tupleValue;

                try {
                    tupleValue = field.get(tuple);

                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error while trying to access instance of class "
                        + tuple.getClass().getSimpleName());
                }

                insertValues.add(tupleValue);
            }
            values(insertValues);
        }

        return getThis();
    }

    // Needed for (inheritance + pattern) \ Warnings
    public abstract T getThis();
}
