package org.docheinstein.sqlbuilder.statements.mysql;

import org.docheinstein.sqlbuilder.Sqlable;
import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.commons.SqlLanguage;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.base.CreateTrigger;

/*
 * CREATE TRIGGER trigger_name BEFORE INSERT ON table_name
 * FOR EACH ROW
 * BEGIN
 *      trigger_content
 * END;
 */
/**
 * Represents a specific CREATE TRIGGER statement that respect MySQL syntax.
 */
public class CreateTriggerMySQL extends CreateTrigger {

    /** Time of the trigger's action. */
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

    /** Type of the trigger's action. */
    public enum ActionType implements Sqlable {
        Insert("INSERT"),
        Update("UPDATE"),
        Delete("DELETE");

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
     * Creates a MySQL CREATE TRIGGER statement.
     * <p>
     * Ensures that {@link org.docheinstein.sqlbuilder.commons.SqlBuilder#setLanguage(SqlLanguage)}
     * has been set properly before create this object.
     * @param triggerName the trigger name
     * @param actionTime the trigger action time
     * @param actionType the trigger action type
     * @param table the table on which create the trigger
     * @param triggerContent the trigger content in MySQL syntax
     *                       (the content is the string between BEGIN and END)
     */
    public CreateTriggerMySQL(String triggerName, ActionTime actionTime,
                              ActionType actionType, Table table,
                              String triggerContent) {
        super(triggerName, table, triggerContent);
        mActionTime = actionTime;
        mActionType = actionType;

        SqlBuilderInternalUtil.throwIfCurrentLanguageIsNot(SqlLanguage.MySQL);
    }

    /**
     * Returns the time of the trigger.
     * @return the action time
     */
    public ActionTime getActionTime() {
        return mActionTime;
    }

    /**
     * Returns the type of the trigger.
     * @return the action type
     */
    public ActionType getActionType() {
        return mActionType;
    }

    @Override
    public String toSql() {
        String sqlStr =  String.format(
            "CREATE TRIGGER %s %s %s ON %s FOR EACH ROW BEGIN %s END;",
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
