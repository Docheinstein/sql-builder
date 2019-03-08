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
     * Null objects are not wrapped, instead null is returned; if there is need
     * to wrap a null value use {@link #ofNull()}.
     * @param o the object
     * @return a sql bindable of the given object
     */
    public static SqlBindable of(Object o) {
        // Do not try to wrap null values, if desired use null()
        if (o == null)
            return null;
        // Do not wrap if the object is already a SqlBindable
        if (o instanceof SqlBindable)
            return (SqlBindable) o;
        // Wrap inside a SqlBindableObject
        return new SqlBindableObject(o);
    }

    /**
     * Returns a sql bindable for a null object.
     * <p>
     * i.e. returns "?" as sql string and null as bindable.
     * @return a sql bindable for a null value
     */
    public static SqlBindable ofNull() {
        return new SqlBindableObject(null);
    }
}
