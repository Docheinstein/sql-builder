package org.docheinstein.sqlbuilder.statements.base;

import org.docheinstein.sqlbuilder.models.Table;

/**
 * Represents a generic CREATE TRIGGER statement.
 * <p>
 * This class is not a concrete implementation of the CREATE TRIGGER statement
 * since it heavily depends on the used SQL language; instead this statement
 * encapsulates the shared stuff between the specific CREATE TRIGGER of each SQL
 * language.
 */
public abstract class CreateTrigger implements SingleShotStatement {

    /** Trigger name. */
    protected final String mTriggerName;

    /** Table on which create the trigger. */
    protected final Table mTable;

    /**
     * Content of the trigger in SQL language.
     * <p>
     * The trigger's content is not checked in any way by this library; the
     * correctness of the trigger's content is left to the end user since
     * it would have been nearly impossible to implement a mechanism to build
     * each possible trigger content (and maybe it would have been unclear
     * at the end of the pain).
     */
    protected final String mTriggerContent;

    /**
     * Creates a generic CREATE TRIGGER statement.
     * @param triggerName the trigger name
     * @param table the table on which create trigger
     * @param triggerContent the content of the trigger in SQL language
     */
    public CreateTrigger(String triggerName, Table table, String triggerContent) {
        mTriggerName = triggerName;
        mTable = table;
        mTriggerContent = triggerContent;
    }

    /**
     * Returns the name of the trigger.
     * @return the trigger's name
     */
    public String getName() {
        return mTriggerName;
    }

    /**
     * Returns the table on which create the trigger
     * @return the trigger's table
     */
    public Table getTable() {
        return mTable;
    }

    /**
     * Returns the content of the trigger.
     * @return the trigger's content
     */
    public String getContent() {
        return mTriggerContent;
    }
}