package org.docheinstein.sqlbuilder.statements.base;

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

    @Override
    public String toSql() {
        StringBuilder sql = new StringBuilder("DROP TRIGGER ");
        if (mIfExists)
            sql.append(" IF EXISTS ");
        sql.append(mTriggerName);
        return sql.toString();
    }
}
