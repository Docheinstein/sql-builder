package org.docheinstein.sqlbuilder.statements.base;

import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;

/**
 * Represents a generic DROP TRIGGER statement.
 * <p>
 * This class is not a concrete implementation of the DROP TRIGGER statement
 * since it heavily depends on the used SQL language; instead this statement
 * encapsulates the shared stuff between the specific DROP TRIGGER of each SQL
 * language.
 */
public abstract class DropTrigger implements SingleShotStatement {

    /** Trigger name. */
    protected final String mTriggerName;

    /** Whether use the IF EXISTS clause for this statement. */
    protected boolean mIfExists;

    /**
     * Creates a DROP TRIGGER statement.
     * @param triggerName the trigger name
     */
    public DropTrigger(String triggerName) {
        mTriggerName = triggerName;
    }

    /**
     * Sets/unsets the IF EXISTS clause.
     * @param ifExists whether set the IF EXISTS clause
     * @return this statement
     */
    public DropTrigger ifExists(boolean ifExists) {
        mIfExists = ifExists;
        return this;
    }

    /**
     * Sets the IF EXISTS clause.
     * @return this statement
     */
    public DropTrigger ifExists() {
        return ifExists(true);
    }

    /**
     * Returns the name of the trigger.
     * @return the trigger's name
     */
    public String getName() {
        return mTriggerName;
    }

    /**
     * Returns whether use the IF EXISTS clause or not.
     * @return true if the IF EXISTS clause has been set
     */
    public boolean getIfExists() {
        return mIfExists;
    }

    @Override
    public String toSql() {
        String sqlStr;

        StringBuilder sql = new StringBuilder("DROP TRIGGER ");
        if (mIfExists)
            sql.append(" IF EXISTS ");
        sql.append(mTriggerName);

        sqlStr = sql.toString();

        SqlBuilderLogger.out("Created [DROP TRIGGER] SQL {" + sqlStr + "}");

        return sqlStr;
    }
}
