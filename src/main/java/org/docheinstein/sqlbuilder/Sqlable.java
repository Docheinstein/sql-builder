package org.docheinstein.sqlbuilder;

/**
 * Represents an entity which can be translated into an SQL string.
 */
public interface Sqlable {

    /**
     * Returns the sql representation of this entity.
     * @return the sql string that represents the current state of this entity.
     */
    String toSql();


}
