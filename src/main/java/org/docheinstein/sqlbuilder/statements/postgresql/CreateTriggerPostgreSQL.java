package org.docheinstein.sqlbuilder.statements.postgresql;

import org.docheinstein.sqlbuilder.Sqlable;
import org.docheinstein.sqlbuilder.commons.SqlBuilderUtil;
import org.docheinstein.sqlbuilder.commons.SqlLanguage;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.base.CreateTrigger;

public class CreateTriggerPostgreSQL extends CreateTrigger {

    public enum ActionTime implements Sqlable {
        Before("BEFORE"),
        After("AFTER"),
        InsteadOf("INSTEAD OF")
        ;

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
        Delete("DELETE"),
        Truncate("TRUNCATE")
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

//    CREATE TRIGGER check_update
//    BEFORE UPDATE ON accounts
//    FOR EACH ROW
//    WHEN (OLD.balance IS DISTINCT FROM NEW.balance)
//    EXECUTE PROCEDURE check_account_update();

//    CREATE TRIGGER check_update
//    BEFORE UPDATE ON accounts
//    FOR EACH ROW
//    EXECUTE PROCEDURE check_account_update();

    public CreateTriggerPostgreSQL(String triggerName, ActionTime actionTime,
                                   ActionType actionType, Table table,
                                   String triggerContent) {
        super(triggerName, triggerContent, table);
        mActionTime = actionTime;
        mActionType = actionType;

        SqlBuilderUtil.ensureLanguage(SqlLanguage.PostgreSQL);
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
