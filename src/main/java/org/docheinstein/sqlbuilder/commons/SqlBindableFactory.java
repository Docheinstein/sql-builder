package org.docheinstein.sqlbuilder.commons;

import org.docheinstein.sqlbuilder.SqlBindable;
import org.docheinstein.sqlbuilder.models.SqlBindableObject;

/**
 * Contains methods for create {@link SqlBindable}
 */
public class SqlBindableFactory {

    /**
     * Returns a sql bindable for the given object.
     * <p>
     * Actually returns a {@link SqlBindableObject} unless the object
     * is itself a {@link SqlBindable}.
     * <p>
     * Null objects are wrapped too.
     * @param o the object
     * @return a sql bindable of the given object
     */
    public static SqlBindable of(Object o) {
        // Do not wrap if the object is already a SqlBindable
        if (o instanceof SqlBindable)
            return (SqlBindable) o;
        // Wrap inside a SqlBindableObject
        return new SqlBindableObject(o);
    }
}
