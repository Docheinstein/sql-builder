package org.docheinstein.sqlbuilder.models;

import org.docheinstein.sqlbuilder.SqlBindable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single bindable object which is translated into '?' for
 * the creation of {@link java.sql.PreparedStatement} and returns the wrapped
 * object as unique bindable object.
 *
 * <p>
 * This is the simplest and most common implementation of {@link SqlBindable}.
 */
public class SqlBindableObject implements SqlBindable {

    /** The bindable objects which actually contains only an object. */
    private final List<Object> mObject = new ArrayList<>();

    /**
     * Creates a bindable object that wraps the given object
     * @param o the object to wrap
     */
    public SqlBindableObject(Object o) {
        mObject.add(o);
    }

    @Override
    public List<Object> getBindableObjects() {
        return mObject;
    }

    @Override
    public String toSql() {
        return "?";
    }
}
