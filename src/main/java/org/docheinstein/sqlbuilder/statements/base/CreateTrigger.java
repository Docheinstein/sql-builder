package org.docheinstein.sqlbuilder.statements.base;

import org.docheinstein.sqlbuilder.models.Table;

public abstract class CreateTrigger extends SingleShotStatement {

    protected final String mTriggerName;
    protected final String mTriggerContent;
    protected final Table mTable;

    public CreateTrigger(String triggerName, String triggerContent, Table table) {
        mTriggerName = triggerName;
        mTriggerContent = triggerContent;
        mTable = table;
    }

    public String getName() {
        return mTriggerName;
    }

    public String getContent() {
        return mTriggerContent;
    }

    public Table getTable() {
        return mTable;
    }
}