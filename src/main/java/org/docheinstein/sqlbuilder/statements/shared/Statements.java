package org.docheinstein.sqlbuilder.statements.shared;

import org.docheinstein.sqlbuilder.models.Column;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.mysql.AlterMySQL;
import org.docheinstein.sqlbuilder.statements.mysql.CreateDatabaseMySQL;
import org.docheinstein.sqlbuilder.statements.mysql.CreateTriggerMySQL;
import org.docheinstein.sqlbuilder.statements.mysql.DropTriggerMySQL;
import org.docheinstein.sqlbuilder.statements.postgresql.CreateTriggerPostgreSQL;
import org.docheinstein.sqlbuilder.statements.postgresql.DropTriggerPostgreSQL;

import java.util.List;

/**
 * Contains methods for create {@link org.docheinstein.sqlbuilder.statements.base.Statement}.
 */
public class Statements {

    // -------------------------------------------------------------------------
    // ------------------------------- DDL -------------------------------------
    // -------------------------------------------------------------------------

    // ------------------------- DDL [DATABASE] --------------------------------

    public static CreateDatabase createDatabase(String database) {
        return new CreateDatabase(database);
    }

    public static CreateDatabaseMySQL createDatabaseMySQL(String database) {
        return new CreateDatabaseMySQL(database);
    }

    public static DropDatabase dropDatabase(String database) {
        return new DropDatabase(database);
    }

    // ------------------------- DDL [TABLES] ----------------------------------

    public static Create create(Table table) {
        return new Create(table);
    }

    public static Delete delete(Table table) {
        return new Delete(table);
    }

    public static Drop drop(Table table) { return new Drop(table); }

    public static Alter alter(Table table) {
        return new Alter(table);
    }

    public static AlterMySQL alterMySQL(Table table) {
        return new AlterMySQL(table);
    }

    // ------------------------- DDL [TRIGGERS] --------------------------------

    public static CreateTriggerMySQL createTrigger(
        String triggerName, CreateTriggerMySQL.ActionTime actionTime,
        CreateTriggerMySQL.ActionType actionType, Table table,
        String triggerContent) {

        return new CreateTriggerMySQL(
            triggerName,
            actionTime,
            actionType,
            table,
            triggerContent
        );
    }

    public static CreateTriggerPostgreSQL createTrigger(
        String triggerName, CreateTriggerPostgreSQL.ActionTime actionTime,
        CreateTriggerPostgreSQL.ActionType actionType, Table table,
        String triggerContent) {
        return new CreateTriggerPostgreSQL(
            triggerName,
            actionTime,
            actionType,
            table,
            triggerContent
        );
    }

    public static DropTriggerMySQL dropTrigger(String triggerName) {
        return new DropTriggerMySQL(triggerName);
    }

    public static DropTriggerPostgreSQL dropTrigger(String triggerName, Table table) {
        return new DropTriggerPostgreSQL(triggerName, table);
    }

    // -------------------------------------------------------------------------
    // ------------------------------- DML -------------------------------------
    // -------------------------------------------------------------------------

    public static Insert insert(Table table) {
        return new Insert(table);
    }

    public static Replace replace(Table table) {
        return new Replace(table);
    }

    public static Update update(Table table) { return new Update(table); }

    // -------------------------------------------------------------------------
    // ------------------------------- DQL -------------------------------------
    // -------------------------------------------------------------------------

    public static Select select(String... columns) {
        return new Select(columns);
    }

    public static Select select(Column... columns) {
        return new Select(columns);
    }

    public static Select select(List<Column> columns) {
        return new Select(columns);
    }
}
