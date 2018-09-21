package org.docheinstein.sqlbuilder.statements.postgresql;

import org.docheinstein.sqlbuilder.Sqlable;
import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.commons.SqlLanguage;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.base.CreateTrigger;

/*
 * CREATE TRIGGER check_update
 * BEFORE UPDATE ON accounts
 * FOR EACH ROW
 * EXECUTE PROCEDURE check_account_update();
 */

/*
 * CREATE TRIGGER check_update
 * BEFORE UPDATE ON accounts
 * content
 */

/**
 * Represents a specific CREATE TRIGGER statement that respect PostgreSQL syntax.
 */
public class CreateTriggerPostgreSQL extends CreateTrigger {

    /** Time of the trigger's action. */
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

    /** Type of the trigger's action. */
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

    /** Time of the trigger's action. */
    private final ActionTime mActionTime;

    /** Type of the trigger's action. */
    private final ActionType mActionType;


    /**
     * Creates a PostgreSQL CREATE TRIGGER statement.
     * <p>
     * Ensures that {@link org.docheinstein.sqlbuilder.commons.SqlBuilder#setLanguage(SqlLanguage)}
     * has been set properly before create this object.
     * @param triggerName the trigger name
     * @param actionTime the trigger action time
     * @param actionType the trigger action type
     * @param table the table on which create the trigger
     * @param triggerContent the trigger content in PostgreSQL syntax
     *                       (the content is everything that comes after
     *                       the ON table part of the trigger)
     */
    public CreateTriggerPostgreSQL(String triggerName, ActionTime actionTime,
                                   ActionType actionType, Table table,
                                   String triggerContent) {
        super(triggerName, table, triggerContent);
        mActionTime = actionTime;
        mActionType = actionType;

        SqlBuilderInternalUtil.throwIfCurrentLanguageIsNot(SqlLanguage.PostgreSQL);
    }

    public ActionTime getActionTime() {
        return mActionTime;
    }

    public ActionType getActionType() {
        return mActionType;
    }

    @Override
    public String toSql() {
        String sqlStr =  String.format(
            "CREATE TRIGGER %s %s %s ON %s %s;",
            mTriggerName,
            mActionTime.toSql(),
            mActionType.toSql(),
            mTable.getName(),
            mTriggerContent
        );

        SqlBuilderLogger.out("Created [CREATE TRIGGER] SQL {" + sqlStr + "}");

        return sqlStr;
    }
}
