package org.docheinstein.sqlbuilder.statements.shared;

/*
 * DROP DATABASE IF EXISTS AmazonDatabase;
 */

import org.docheinstein.sqlbuilder.statements.base.SingleShotStatement;

/**
 * Represents a DROP DATABASE statement.
 */
public class DropDatabase implements SingleShotStatement {

    private final String mDatabaseName;

    private boolean mIfExists;

    /**
     * Creates a DROP DATABASE statement for the given database name.
     * @param database the name of the database to drop
     */
    public DropDatabase(String database) {
        mDatabaseName = database;
    }

    /**
     * Adds the IF NOT EXISTS clause.
     * @return this statement
     */
    public DropDatabase ifExists() {
        return ifExists(true);
    }

    /**
     * Sets/unsets the IF EXISTS clause.
     * @param ifExists whether set the IF EXISTS clause
     * @return this statement
     */
    public DropDatabase ifExists(boolean ifExists) {
        mIfExists = ifExists;
        return this;
    }

    @Override
    public String toSql() {
        return
            "DROP DATABASE " +
                (mIfExists ? "IF EXISTS " : "") +
                mDatabaseName;
    }
}
