package org.docheinstein.sqlbuilder.statements.mysql;

import org.docheinstein.sqlbuilder.Sqlable;
import org.docheinstein.sqlbuilder.commons.SqlBuilderUtil;
import org.docheinstein.sqlbuilder.commons.SqlLanguage;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.base.CreateTrigger;

public class CreateTriggerMySQL extends CreateTrigger {

    public enum ActionTime implements Sqlable {
        Before("BEFORE"),
        After("AFTER");

        ActionTime(String sql) {
            mSql = sql;
        }

        private String mSql;

        @Override
        public String toSql() {
            return mSql;
        }
    }

    public enum ActionType implements Sqlable {
        Insert("INSERT"),
        Update("UPDATE"),
        Delete("DELETE")
        ;

        ActionType(String sql) {
            mSql = sql;
        }

        private String mSql;

        @Override
        public String toSql() {
            return mSql;
        }
    }

    private final ActionTime mActionTime;
    private final ActionType mActionType;

    // CREATE TRIGGER trigger_name BEFORE INSERT ON table_name
    // FOR EACH ROW
    // BEGIN
    //      ...
    // END;
    public CreateTriggerMySQL(String triggerName, ActionTime actionTime,
                              ActionType actionType, Table table,
                              String triggerContent) {
        super(triggerName, triggerContent, table);
        mActionTime = actionTime;
        mActionType = actionType;

        SqlBuilderUtil.ensureLanguage(SqlLanguage.MySQL);
    }

    public ActionTime getActionTime() {
        return mActionTime;
    }

    public ActionType getActionType() {
        return mActionType;
    }

    @Override
    public String toSql() {
        return String.format(
            "CREATE TRIGGER %s %s %s ON %s FOR EACH ROW BEGIN %s END;",
            mTriggerName,
            mActionTime.toSql(),
            mActionType.toSql(),
            mTable.getName(),
            mTriggerContent
        );
    }
}
