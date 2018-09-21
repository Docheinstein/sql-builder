package org.docheinstein.sqlbuilder.clauses;

import org.docheinstein.sqlbuilder.Sqlable;
import org.docheinstein.sqlbuilder.models.Column;

/**
 * Represents a JOIN condition between two columns in a SELECT statement.
 * @param <T> the type of the columns
 */
public class Join<T> {

    /**
     * Type of join.
     */
    /*
     * SELECT * FROM table <Join.Type> JOIN
     */
    public enum Type implements Sqlable {
        Inner("INNER"),
        Left("LEFT"),
        Right("RIGHT"),
        Full("FULL");

        Type(String sql) {
            mSql = sql;
        }

        private String mSql;

        @Override
        public String toSql() {
            return mSql;
        }

    }

    /** Type of join. */
    private Type mType;

    /** First column involved in the join. */
    private Column<T> mInternalColumn;

    /** Second column involved in the join. */
    private Column<T> mExternalColumn;

    public Join(Type type, Column<T> external, Column<T> internal) {
        mType = type;
        mExternalColumn = external;
        mInternalColumn = internal;
    }

    /**
     * Returns the type of join.
     * @return the type of join.
     */
    public Type getType() {
        return mType;
    }

    /**
     * Returns the second column involved in the join.
     * <p>
     * Note that although the method's name says external the column
     * can even be on the same table of the internal column (auto join).
     * @return the second column involved in the join
     */
    public Column<T> getExternalColumn() {
        return mExternalColumn;
    }

    /**
     * Returns the first column involved in the join.
     * @return the first column involved in the join
     */
    public Column<T> getInternalColumn() {
        return mInternalColumn;
    }
}
