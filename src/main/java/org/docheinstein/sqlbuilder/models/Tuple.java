package com.docheinstein.sqlbuilder.models;

import com.docheinstein.sqlbuilder.annotations.ColumnField;
import com.docheinstein.sqlbuilder.statements.Select;
import com.docheinstein.sqlbuilder.statements.Update;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a tuple (row) of a database table.
 *
 * Every field annotated with {@link ColumnField} will be exposed
 * to the builder while setting/getting into/from the database.
 * This can be useful in combination with
 * {@link Select#fetch(Connection, Class)}
 * or {@link Update#setsFromTuple(Tuple)}.
 * The class that implements this interface can have other fields not annotated
 * with {@link ColumnField}; those will be ignored by the builder (but might
 * be useful to you for something else).
 *
 * Any class implementing this interface MUST provide a default constructor
 * in order to let the builder build an object of this type while retrieving
 * values from the database through SELECT.
 */
public interface Tuple {

    /**
     * Returns a map that associate the name given to a field
     * by the {@link ColumnField} annotation and the field.
     * @return a map that associated column names to fields.
     */
    default Map<String, Field> getColumnFieldMap() {
        Map<String, Field> columnFieldMap = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(ColumnField.class)) {
                ColumnField columnFieldAnnotation = field.getAnnotation(ColumnField.class);
                field.setAccessible(true);
                columnFieldMap.put(columnFieldAnnotation.value(), field);
            }
        }

        return columnFieldMap;
    }
}
