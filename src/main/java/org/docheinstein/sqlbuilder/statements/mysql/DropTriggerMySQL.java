package org.docheinstein.sqlbuilder.statements.mysql;

import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.commons.SqlLanguage;
import org.docheinstein.sqlbuilder.statements.base.DropTrigger;

public class DropTriggerMySQL extends DropTrigger {

    public DropTriggerMySQL(String triggerName) {
        super(triggerName);

        SqlBuilderInternalUtil.ensureLanguage(SqlLanguage.MySQL);
    }
}
