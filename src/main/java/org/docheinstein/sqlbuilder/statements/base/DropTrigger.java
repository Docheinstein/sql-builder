package org.docheinstein.sqlbuilder.statements.base;

import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;

public abstract class DropTrigger extends SingleShotStatement {

    protected final String mTriggerName;
    protected boolean mIfExists;

    public DropTrigger(String triggerName) {
        mTriggerName = triggerName;
    }

    public DropTrigger ifExists(boolean ifExists) {
        mIfExists = ifExists;
        return this;
    }

    public DropTrigger ifExists() {
        return ifExists(true);
    }

    public String getName() {
        return mTriggerName;
    }

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
