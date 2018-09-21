package org.docheinstein.sqlbuilder.statements.postgresql;

import org.docheinstein.sqlbuilder.Sqlable;
import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.commons.SqlLanguage;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.base.DropTrigger;

/**
 * Represents a specific DROP TRIGGER statement that respect PostgreSQL syntax.
 */
public class DropTriggerPostgreSQL extends DropTrigger {

    /** Drop action. */
    public enum DropOption implements Sqlable {
        Cascade("CASCADE"),
        Restrict("RESTRICT"),
        ;

        DropOption(String sql) {
            mSql = sql;
        }

        private String mSql;

        @Override
        public String toSql() {
            return mSql;
        }
    }

    /** Table from which drop the trigger. */
    private final Table mTable;

    /** Drop option. */
    private DropOption mDropOption;

    /**
     * Creates a DROP TRIGGER statement using MySQL syntax.
     * <p>
     * Ensures that {@link org.docheinstein.sqlbuilder.commons.SqlBuilder#setLanguage(SqlLanguage)}
     * has been set properly before create this object.
     * @param triggerName the trigger name
     * @param table the name of the table from which drop the trigger
     */
    public DropTriggerPostgreSQL(String triggerName, Table table) {
        super(triggerName);
        mTable = table;

        SqlBuilderInternalUtil.throwIfCurrentLanguageIsNot(SqlLanguage.PostgreSQL);
    }

    /**
     * Sets CASCADE as drop option.
     * @return this statement
     */
    public DropTriggerPostgreSQL cascade() {
        return option(DropOption.Cascade);
    }

    /**
     * Sets the drop option for this statement
     * @param dropOption the drop option
     * @return this statement
     */
    public DropTriggerPostgreSQL option(DropOption dropOption) {
        mDropOption = dropOption;
        return this;
    }

    /**
     * Sets RESTRICT as drop option.
     * @return this statement
     */
    public DropTriggerPostgreSQL restrict() {
        return option(DropOption.Restrict);
    }

    /**
     * Returns the table from which the trigger has to be dropped.
     * @return the table from which drop the trigger
     */
    public Table getTable() {
        return mTable;
    }

    /**
     * Returns the drop action tu use.
     * @return the drop action of this statement
     */
    public DropOption getDropOption() {
        return mDropOption;
    }

    @Override
    public String toSql() {
        String sqlStr = super.toSql() + " ON TABLE " + mTable.getName();

        if (mDropOption != null)
            sqlStr += " " + mDropOption.toSql();

        SqlBuilderLogger.out("Created [DROP TRIGGER] SQL {" + sqlStr + "}");

        return sqlStr;
    }
}
