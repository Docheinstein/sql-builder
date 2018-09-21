package org.docheinstein.sqlbuilder.statements.mysql;

import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.commons.SqlLanguage;
import org.docheinstein.sqlbuilder.statements.base.DropTrigger;

/**
 * Represents a specific DROP TRIGGER statement that respect MySQL syntax.
 */
public class DropTriggerMySQL extends DropTrigger {

    /**
     * Creates a DROP TRIGGER statement using MySQL syntax.
     * <p>
     * Ensures that {@link org.docheinstein.sqlbuilder.commons.SqlBuilder#setLanguage(SqlLanguage)}
     * has been set properly before create this object.
     * @param triggerName the trigger name
     */
    public DropTriggerMySQL(String triggerName) {
        super(triggerName);

        SqlBuilderInternalUtil.throwIfCurrentLanguageIsNot(SqlLanguage.MySQL);
    }
}
