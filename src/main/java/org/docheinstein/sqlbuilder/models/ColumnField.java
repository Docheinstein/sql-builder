package org.docheinstein.sqlbuilder.models;

import org.docheinstein.sqlbuilder.statements.shared.Select;
import org.docheinstein.sqlbuilder.statements.shared.Update;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.sql.Connection;

/**
 * Represents a field of an entity that have to be mapped to column of a row
 * in a database's table.
 *
 * <p>
 *
 * This can be useful in combination with {@link Select#fetch(Connection, Class)}
 * or {@link Update#setFromTuple(Tuple)}.
 *
 * <p>
 *
 * The value provided MUST match the name of the column this field should be
 * mapped to; by the way the field itself might have any name.
 *
 * e.g.
 *
 * @ColumnField("Username")
 * private String mUsername;
 *
 * @see Tuple
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnField {
    /**
     * The table's column's name.
     * @return the table's column's name
     */
    String value();
}
