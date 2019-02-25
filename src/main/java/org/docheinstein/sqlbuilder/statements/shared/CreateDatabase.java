package org.docheinstein.sqlbuilder.statements.shared;

/*
 * CREATE DATABASE IF NOT EXISTS AmazonDatabase;
 */

import org.docheinstein.sqlbuilder.statements.base.SingleShotStatement;

/**
 * Represents a CREATE DATABASE statement.
 */
public class CreateDatabase implements SingleShotStatement {

    protected final String mDatabaseName;

    /**
     * Creates a CREATE DATABASE statement for the given database name.
     * @param database the name of the database to create
     */
    public CreateDatabase(String database) {
        mDatabaseName = database;
    }

    @Override
    public String toSql() {
        return "CREATE DATABASE " + mDatabaseName;
    }
}
