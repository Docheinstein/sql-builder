package org.docheinstein.sqlbuilder.clauses;

import org.docheinstein.sqlbuilder.Sqlable;
import org.docheinstein.sqlbuilder.models.Column;

/**
 * Represents a foreign key constraint between the columns of different tables
 * in a CREATE TABLE statement.
 * @param <T> the type of the columns
 */
public class ForeignKey<T> {

    /**
     * Action to perform when a foreign key is violated.
     * <p>
     * Can be used either for ON DELETE or ON UPDATE conditions.
     */
    /*
     * REFERENCES table ON DELETE <ForeignKey.ReferenceAction>
     * REFERENCES table ON UPDATE <ForeignKey.ReferenceAction>
     */
    public enum ReferenceAction implements Sqlable {
        NoAction("NO ACTION"),
        Restrict("RESTRICT"),
        Cascade("CASCADE"),
        SetNull("SET NULL"),
        SetDefault("SET DEFAULT")
        ;

        ReferenceAction(String sql) {
            mSql = sql;
        }

        private String mSql;

        @Override
        public String toSql() {
            return mSql;
        }
    }

    /** The column of the first table involved in the constraint. */
    private Column<T> mInternalColumn;

    /** The column of the second table involved in the constraint. */
    private Column<T> mExternalColumn;


    /** The action to perform when a row involved in the constraint is deleted. */
    // REFERENCES table ON DELETE <ReferenceAction>
    private ReferenceAction mOnDelete;

    /** The action to perform when a row involved in the constraint is updated. */
    // REFERENCES table ON UPDATE <ReferenceAction>
    private ReferenceAction mOnUpdate;

    /**
     * Creates a foreign key.
     * @param internalCol a column of a table
     * @param externalCol another column of another table with the same type of the first
     * @param onDelete an optional ON DELETE action
     * @param onUpdate an optional ON UPDATE action
     */
    public ForeignKey(Column<T> internalCol, Column<T> externalCol,
                      ReferenceAction onDelete, ReferenceAction onUpdate) {
        mInternalColumn = internalCol;
        mExternalColumn = externalCol;
        mOnDelete = onDelete;
        mOnUpdate = onUpdate;
    }

    /**
     * Creates a foreign key.
     * @param internalCol a column of a table
     * @param externalCol another column of another table with the same type of the first
     */
    public ForeignKey(Column<T> internalCol, Column<T> externalCol) {
        this(internalCol, externalCol, null, null);
    }

    /**
     * Sets the action to perform when a row involved in the constraint is deleted.
     * @param what the action
     * @return this foreign key
     */
    public ForeignKey onDelete(ReferenceAction what) {
        mOnDelete = what;
        return this;
    }

    /**
     * Sets the action to perform when a row involved in the constraint is updated.
     * @param what the action
     * @return this foreign key
     */
    public ForeignKey onUpdate(ReferenceAction what) {
        mOnUpdate = what;
        return this;
    }

    /**
     * Returns the first column of the constraint.
     * @return the first column of the constraint
     */
    public Column<T> getInternalColumn() {
        return mInternalColumn;
    }

    /**
     * Returns the second column of the constraint.
     * @return the second column of the constraint
     */
    public Column<T> getExternalColumn() {
        return mExternalColumn;
    }

    /**
     * Returns the ON DELETE action.
     * @return the ON DELETE action
     */
    public ReferenceAction getOnDeleteOption() {
        return mOnDelete;
    }

    /**
     * Returns the ON UPDATE action.
     * @return the ON UPDATE action
     */
    public ReferenceAction getOnUpdateOption() {
        return mOnUpdate;
    }
}
