package org.docheinstein.sqlbuilder.statements.postgresql;

import org.docheinstein.sqlbuilder.Sqlable;
import org.docheinstein.sqlbuilder.commons.SqlBuilderUtil;
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

        SqlBuilderUtil.ensureLanguage(SqlLanguage.PostgreSQL);
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
        String sql = super.toSql() + " ON TABLE " + mTable.getName();
        if (mDropOption != null)
            sql += " " + mDropOption.toSql();
        return sql;
    }
}
