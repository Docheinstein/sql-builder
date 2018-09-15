package org.docheinstein.sqlbuilder.statements.mysql;

import org.docheinstein.sqlbuilder.commons.SqlBuilderUtil;
import org.docheinstein.sqlbuilder.commons.SqlLanguage;
import org.docheinstein.sqlbuilder.statements.base.DropTrigger;

public class DropTriggerMySQL extends DropTrigger {

    public DropTriggerMySQL(String triggerName) {
        super(triggerName);

        SqlBuilderUtil.ensureLanguage(SqlLanguage.MySQL);
    }
}
