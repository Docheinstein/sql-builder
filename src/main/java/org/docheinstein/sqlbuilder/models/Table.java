package org.docheinstein.sqlbuilder.models;

import org.docheinstein.sqlbuilder.clauses.ForeignKey;
import org.docheinstein.sqlbuilder.commons.SqlLanguage;
import org.docheinstein.sqlbuilder.expressions.Expression;
import org.docheinstein.sqlbuilder.statements.mysql.CreateTriggerMySQL;
import org.docheinstein.sqlbuilder.statements.postgresql.CreateTriggerPostgreSQL;
import org.docheinstein.sqlbuilder.statements.postgresql.DropTriggerPostgreSQL;
import org.docheinstein.sqlbuilder.statements.shared.*;
import org.docheinstein.sqlbuilder.types.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a database table.
 */
public class Table {
    /** Name of the table. */
    private String mName;

    /** Columns of the table. */
    private List<Column> mColumns = new ArrayList<>();

    /** Primary key(s) of the table. */
    private List<String> mPrimaryKey = new ArrayList<>();

    /** Foreign key(s) of the table. */
    private List<ForeignKey> mForeignKeys = new ArrayList<>();

    /** Check expression of the table. */
    private Expression mCheck;

    /**
     * Creates a table with the given table name.
     * @param name the name of the table
     */
    public Table(String name) {
        mName = name;
    }

    // -------------------------------------------------------------------------
    // ----------------------------- DDL ---------------------------------------
    // -------------------------------------------------------------------------

    /**
     * Adds a column to this table and returns the table
     * @param name the name of the column
     * @param type the type of the column
     * @param <T> the underlying java type wrapped by the column
     * @return this table
     */
    public <T> Table col(String name, Type<T> type) {
        return col(new Column<>(this, name, type));
    }

    /**
     * Adds a column to this table and returns the table.
     * @param col the column
     * @return this table
     */
    public Table col(Column col) {
        mColumns.add(col);
        return this;
    }

    /**
     * Adds primary key(s) to this table.
     * @param columns the column(s) to use as primary key(s)
     * @return this table
     */
    public Table primaryKey(String... columns) {
        mPrimaryKey.addAll(Arrays.asList(columns));
        return this;
    }

    /**
     * Adds primary key(s) to this table.
     * @param columns the column(s) to use as primary key(s)
     * @return this table
     */
    public Table primaryKey(Column... columns) {
        for (Column c : columns)
            mPrimaryKey.add(c.getName());
        return this;
    }

    /**
     * Adds a foreign key to this table.
     * @param fk the foreign key to add
     * @return this table
     */
    public Table foreignKey(ForeignKey fk) {
        mForeignKeys.add(fk);
        return this;
    }

    /**
     * Adds a foreign key to this table.
     * @param internalCol the internal column for the foreign key (the one
     *                    from this table)
     * @param externalCol the external column for the foreign key (typically
     *                    the one from the other table)
     * @param onDelete the ON DELETE action for the foregin key
     * @param onUpdate the ON UPDATE action for the foreign key
     * @param <T> the type of the columns (must be the same)
     * @return this table
     */
    public <T> Table foreignKey(Column<T> internalCol, Column<T> externalCol,
                               ForeignKey.ReferenceAction onDelete,
                               ForeignKey.ReferenceAction onUpdate) {
        return foreignKey(new ForeignKey<>(internalCol, externalCol, onDelete, onUpdate));
    }

    /**
     * Adds a foreign key to this table without ON DELETE or ON UPDATE clauses.
     * @param internalCol the internal column for the foreign key (the one
     *                    from this table)
     * @param externalCol the external column for the foreign key (typically
     *                    the one from the other table)
     * @param <T> the type of the columns (must be the same)
     * @return this table
     *
     * @see #foreignKey(Column, Column, ForeignKey.ReferenceAction, ForeignKey.ReferenceAction)
     */
    public <T> Table foreignKey(Column<T> internalCol, Column<T> externalCol) {
        return foreignKey(new ForeignKey<>(internalCol, externalCol, null, null));
    }

    /**
     * Adds a CHECK expression to this table
     * @param e the CHECK expression
     * @return this table
     */
    public Table check(Expression e) {
        mCheck = e;
        return this;
    }

    // -------------------------------------------------------------------------
    // --------------------------- UTILITIES -----------------------------------
    // -------------------------------------------------------------------------

    // DDL

    /**
     * Returns the table's columns.
     * @return the columns
     */
    public List<Column> getColumns() {
        return mColumns;
    }

    /**
     * Returns the table's primary key.
     * @return the primary key
     */
    public List<String> getPrimaryKey() {
        return mPrimaryKey;
    }

    /**
     * Returns the table's foreign key.
     * @return the foreign key
     */
    public List<ForeignKey> getForeignKeys() {
        return mForeignKeys;
    }

    /**
     * Returns the table's CHECK condition.
     * @return the CHECK condition
     */
    public Expression getCheck() {
        return mCheck;
    }

    // GENERAL

    /**
     * Returns the table name.
     * @return the table name
     */
    public String getName() {
        return mName;
    }

    // -------------------------------------------------------------------------
    // ----------------------------- STATEMENTS --------------------------------
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // ---------------------------------- DDL ----------------------------------
    // -------------------------------------------------------------------------

    /**
     * Creates a CREATE TABLE statement for this table.
     * @return a CREATE TABLE statement
     */
    public Create create() {
        return Statements.create(this);
    }

    /**
     * Creates a DROP TABLE statement for this table.
     * @return a DROP TABLE statement
     */
    public Drop drop() {
        return Statements.drop(this);
    }

    /**
     * Creates a ALTER TABLE statement for this table.
     * @return a ALTER TABLE statement
     */
    public Alter alter() { return Statements.alter(this); }

    /**
     * Creates a CREATE TRIGGER (with MySQL syntax) statement for this table.
     * <p>
     * Before call this method ensure that
     * {@link org.docheinstein.sqlbuilder.commons.SqlBuilder#setLanguage(SqlLanguage)}
     * has been called using {@link SqlLanguage#MySQL} as language.
     * @param triggerName the trigger name
     * @param actionTime the trigger time
     * @param actionType the trigger type
     * @param triggerContent the trigger content
     * @return a CREATE TRIGGER statement for MySQL
     */
    public CreateTriggerMySQL createTrigger(String triggerName,
                                            CreateTriggerMySQL.ActionTime actionTime,
                                            CreateTriggerMySQL.ActionType actionType,
                                            String triggerContent) {
        return Statements.createTrigger(
            triggerName, actionTime, actionType,
            this, triggerContent
        );
    }

    /**
     * Creates a CREATE TRIGGER (with PostgreSQL syntax) statement for this table.
     * <p>
     * Before call this method ensure that
     * {@link org.docheinstein.sqlbuilder.commons.SqlBuilder#setLanguage(SqlLanguage)}
     * has been called using {@link SqlLanguage#PostgreSQL} as language.
     * @param triggerName the trigger name
     * @param actionTime the trigger time
     * @param actionType the trigger type
     * @param triggerContent the trigger content
     * @return a CREATE TRIGGER statement for PostgreSQL
     */
    public CreateTriggerPostgreSQL createTrigger(
        String triggerName,
        CreateTriggerPostgreSQL.ActionTime actionTime,
        CreateTriggerPostgreSQL.ActionType actionType,
        String triggerContent) {
        return Statements.createTrigger(
            triggerName, actionTime, actionType,
            this, triggerContent
        );
    }

    // DropTriggerMySQL is not present since the the statement doesn't require this table name

    /**
     * Creates a DROP TRIGGER (with PostgreSQL syntax) statement for this table.
     * <p>
     * Before call this method ensure that
     * {@link org.docheinstein.sqlbuilder.commons.SqlBuilder#setLanguage(SqlLanguage)}
     * has been called using {@link SqlLanguage#PostgreSQL} as language.
     * @param triggerName the trigger name
     * @return a DROP TRIGGER statement for PostgreSQL
     */
    public DropTriggerPostgreSQL dropTrigger(String triggerName) {
        return Statements.dropTrigger(triggerName, this);
    }

    // -------------------------------------------------------------------------
    // ---------------------------------- DML ----------------------------------
    // -------------------------------------------------------------------------

    /**
     * Creates a DELETE FROM TABLE statement for this table.
     * @return a DELETE FROM TABLE statement
     */
    public Delete delete() {
        return Statements.delete(this);
    }

    /**
     * Creates a INSERT INTO statement for this table.
     * @return a INSERT INTO statement
     */
    public Insert insert() {
        return Statements.insert(this);
    }

    /**
     * Creates a REPLACE statement for this table.
     * @return a REPLACE statement
     */
    public Replace replace() {
        return Statements.replace(this);
    }

    /**
     * Creates a UPDATE statement for this table.
     * @return a UPDATE statement
     */
    public Update update() {
        return Statements.update(this);
    }

    // -------------------------------------------------------------------------
    // ---------------------------------- DQL ----------------------------------
    // -------------------------------------------------------------------------

    /**
     * Creates a SELECT statement for this table.
     * @param columns the columns to select
     * @return a SELECT statement
     */
    public Select select(String... columns) {
        return Statements.select(columns).from(this);
    }

    /**
     * Creates a SELECT statement for this table.
     * @param columns the columns to select
     * @return a SELECT statement
     */
    public Select select(Column... columns) {
        return Statements.select(columns).from(this);
    }

    /**
     * Creates a SELECT statement for this table.
     * @param columns the columns to select
     * @return a SELECT statement
     */
    public Select select(List<Column> columns) {
        return Statements.select(columns).from(this);
    }
}
