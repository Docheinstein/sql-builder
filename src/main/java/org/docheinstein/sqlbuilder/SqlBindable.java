package org.docheinstein.sqlbuilder;

import java.util.List;

/**
 * Represents an {@link Sqlable} which can be translated into an SQL string
 * and moreover provides a list of object which should be bound to this entity
 * while creating a {@link java.sql.PreparedStatement}.
 */
public interface SqlBindable extends Sqlable {

    /**
     * Returns the list of object that should be bound to this entity
     * for create a {@link java.sql.PreparedStatement}.
     * @return a list of object to bound to this entity
     */
    List<Object> getBindableObjects();
}
