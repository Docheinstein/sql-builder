package org.docheinstein.sqlbuilder.statements.mysql;

import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.commons.SqlLanguage;
import org.docheinstein.sqlbuilder.statements.shared.CreateDatabase;

public class CreateDatabaseMySQL extends CreateDatabase {

    private boolean mIfNotExists;

    /**
     * Creates a MysQL CREATE DATABASE statement for the given database name.
     * <p>
     * Ensures that {@link org.docheinstein.sqlbuilder.commons.SqlBuilder#setLanguage(SqlLanguage)}
     * has been set properly before create this object.
     * @param database the name of the database to create
     */
    public CreateDatabaseMySQL(String database) {
        super(database);

        SqlBuilderInternalUtil.throwIfCurrentLanguageIsNot(SqlLanguage.MySQL);
    }

    /**
     * Adds the IF NOT EXISTS clause.
     * @return this statement
     */
    public CreateDatabaseMySQL ifNotExists() {
        return ifNotExists(true);
    }

    /**
     * Sets/unsets the IF NOT EXISTS clause.
     * @param ifNotExists whether set the IF NOT EXISTS clause
     * @return this statement
     */
    public CreateDatabaseMySQL ifNotExists(boolean ifNotExists) {
        mIfNotExists = ifNotExists;
        return this;
    }

    @Override
    public String toSql() {
        return
            "CREATE DATABASE " +
            (mIfNotExists ? "IF NOT EXISTS " : "") +
            mDatabaseName;
    }
}
