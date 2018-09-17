package org.docheinstein.sqlbuilder.statements.postgresql;

import org.docheinstein.sqlbuilder.Sqlable;
import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.commons.SqlLanguage;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.base.DropTrigger;

public class DropTriggerPostgreSQL extends DropTrigger {

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

    private final Table mTable;
    private DropOption mDropOption;

    public DropTriggerPostgreSQL(String triggerName, Table table) {
        super(triggerName);
        mTable = table;

        SqlBuilderInternalUtil.ensureLanguage(SqlLanguage.PostgreSQL);
    }

    public DropTriggerPostgreSQL option(DropOption dropOption) {
        mDropOption = dropOption;
        return this;
    }

    public DropTriggerPostgreSQL restrict() {
        return option(DropOption.Restrict);
    }

    public DropTriggerPostgreSQL cascade() {
        return option(DropOption.Cascade);
    }

    public Table getTable() {
        return mTable;
    }

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
