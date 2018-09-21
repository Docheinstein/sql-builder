package org.docheinstein.sqlbuilder.statements.base;

import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.models.Column;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.models.Tuple;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Represents an generic statement that puts data into the
 * database (INSERT, REPLACE).
 */
public abstract class Put<T extends Put<T>> implements UpdateStatement {

    /** Table this statements affect. */
    protected Table mTable;

    /**
     * Values to insert into the table.
     * <p>
     * This is a list of a list of object since more than a tuple could be
     * inserted per statement's execution and each tuple contains more values.
     **/
    protected List<List<Object>> mValuesList = new ArrayList<>();

    /**
     * Creates a put statement for the given table.
     * @param table the table on which put data
     */
    public Put(Table table) {
        mTable = table;
    }

    /**
     * Adds the given values to the list of values to put into the table.
     * @param values a list of values
     * @return this statement
     */
    public T values(Object... values) {
        return values(Arrays.asList(values));
    }

    /**
     * Adds the given values to the list of values to put into the table.
     * @param values a list of values
     * @return this statement
     */
    public T values(List<Object> values) {
        mValuesList.add(values);
        return getThis();
    }

    /**
     * Adds the values of each tuple to the list of values to put into the table.
     * @param tuples a list of tuples to put into the table
     * @return this statement
     */
    public T valuesFromTuples(Tuple... tuples) {
        // Tuples must be of the same type
        if (tuples == null || tuples.length <= 0)
            return getThis();


        Map<String, Field> columnFieldMap;

        try {
            // Retrieves the column field map
            columnFieldMap = SqlBuilderInternalUtil.getColumnFields(tuples[0].getClass());
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException(
                "Error while trying to access column field of class " +
                    tuples[0].getClass().getName() +
                    ". (The class must have a default constructor)");
        }

        // For each tuples scans every columns and for each of those
        // retrieve the values from the tuple

        for (Tuple tuple : tuples) {
            List<Column> tableColumns = mTable.getColumns();
            List<Object> insertValues = new ArrayList<>();

            for (Column column : tableColumns) {
                Field field = columnFieldMap.get(column.getName());
                if (field == null)
                    throw new RuntimeException("The tuple of class " +
                        tuple.getClass().getSimpleName() +
                        " doesn't have an annotation for column " +
                        column.getName() + " of the table " + mTable.getName());

                Object tupleValue;

                try {
                    tupleValue = field.get(tuple);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error while trying to access instance of class "
                        + tuple.getClass().getSimpleName());
                }

                insertValues.add(tupleValue);
            }

            // Insert each legal value of this tuple to the list of values
            values(insertValues);
        }

        return getThis();
    }

    // Needed for (inheritance + generics) \ Warnings

    /**
     * Returns this statement.
     * <p>
     * This might seem dummy but is the only way to implement
     * inheritance + generics + builder pattern without a warning from the compiler
     * @return this statement
     */
    public abstract T getThis();
}
