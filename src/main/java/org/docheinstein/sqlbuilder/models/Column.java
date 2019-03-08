package org.docheinstein.sqlbuilder.models;

import org.docheinstein.sqlbuilder.SqlBindable;
import org.docheinstein.sqlbuilder.Sqlable;
import org.docheinstein.sqlbuilder.expressions.Operators;
import org.docheinstein.sqlbuilder.expressions.Expression;
import org.docheinstein.sqlbuilder.statements.shared.Select;
import org.docheinstein.sqlbuilder.types.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a column of {@link Table}.
 * @param <T> the java type this column is mapped to.
 */
public class Column<T> implements SqlBindable {

    /** Name of the table this column belongs to. */
    private String mTable;

    /** Column name. */
    private String mColumnName;

    /** Column type. */
    private Type<T> mColumnType;

    /** Whether this column has AUTO_INCREMENT flag. */
    private boolean mAutoIncrement = false;

    /** Whether this column is primary key. */
    private boolean mPrimaryKey = false;

    /** Whether this column has NOT NULL flag. */
    private boolean mNotNull = false;

    /** Default value for the column (DEFAULT). */
    private Object mDefault = null;

    /**
     * Creates a column for the given table, column name and type.
     * @param table the table this column belongs to
     * @param name the column name
     * @param type the column type
     */
    public Column(Table table, String name, Type<T> type) {
        this(table != null ? table.getName() : null, name, type);
    }

    /**
     * Creates a column for the given table, column name and type.
     * <p>
     * This constructor MUST be used if the column is created within the creation
     * of the table since pass the table object instead of the table name as
     * string would lead to a failure in calling table.getName().
     * @param tableName the table this column belongs to
     * @param name the column name
     * @param type the column type
     */
    public Column(String tableName, String name, Type<T> type) {
        mTable = tableName;
        mColumnName = name;
        mColumnType = type;
    }

    // -------------------------------------------------------------------------
    // ----------------------------- DDL ---------------------------------------
    // -------------------------------------------------------------------------

    /**
     * Sets/unsets the PRIMARY KEY flag.
     * @param primaryKey whether this column has the PRIMARY KEY flag
     * @return this column
     */
    public Column<T> primaryKey(boolean primaryKey) {
        mPrimaryKey = primaryKey;
        return this;
    }

    /**
     * Sets the PRIMARY KEY flag.
     * @return this column
     */
    public Column<T> primaryKey() {
        return primaryKey(true);
    }

    /**
     * Sets/unsets the NOT NULL flag.
     * @param notNull whether this column has the NOT NULL flag.
     * @return this column
     */
    public Column<T> notNull(boolean notNull) {
        mNotNull = notNull;
        return this;
    }

    /**
     * Sets the NOT NULL flag.
     * @return this column
     */
    public Column<T> notNull() {
        return notNull(true);
    }

    /**
     * Sets the AUTO_INCREMENT flag
     * @return this column
     */
    public Column<T> autoIncrement() {
        return autoIncrement(true);
    }

    /**
     * Sets/unsets the AUTO_INCREMENT flag
     * @param autoIncrement whether this column has the AUTO_INCREMENT flag
     * @return this column
     */
    public Column<T> autoIncrement(boolean autoIncrement) {
        mAutoIncrement = autoIncrement;
        return this;
    }

    /**
     * Sets the DEFAULT value for the column.
     * @param value the default value
     * @return this column
     */
    // This is preferred over the generic defaultValueObject() for have type control
    public Column<T> defaultValue(T value) {
        mDefault = value;
        return this;
    }

    /**
     * Sets the DEFAULT value for the column as generic object on which will
     * be invoked the toString() method.
     * @param value the default value
     * @return this column
     */
    public Column<T> defaultValueObject(Object value) {
        mDefault = value;
        return this;
    }


    // -------------------------------------------------------------------------
    // --------------------------- UTILITIES -----------------------------------
    // -------------------------------------------------------------------------

    // DDL

    /**
     * Returns whether this column has the PRIMARY KEY flag.
     * @return whether this column has the PRIMARY KEY flag
     */
    public boolean isPrimaryKey() {
        return mPrimaryKey;
    }

    /**
     * Returns whether this column has the NOT NULL flag.
     * <p>
     * Note that this is VERY DIFFERENT from {@link #isNotNull()} since the
     * former belongs to DDL while the latter is used for create queries.
     * @return whether this column has the NOT NULL flag
     */
    public boolean getNotNull() {
        return mNotNull;
    }

    /**
     * Returns whether this column has the AUTO_INCREMENT flag.
     * @return whether this column has the AUTO_INCREMENT flag
     */
    public boolean isAutoIncrement() {
        return mAutoIncrement;
    }

    /**
     * Returns whether this column has the DEFAULT value.
     * @return whether this column has the DEFAULT value
     */
    public boolean hasDefaultValue() {
        return mDefault != null;
    }

    // GENERAL

    /**
     * Returns the name of the table this column belongs to.
     * @return the table name
     */
    public String getTable() {
        return mTable;
    }

    /**
     * Returns the column name.
     * @return the column name
     */
    public String getName() {
        return mColumnName;
    }

    /**
     * Returns the column type.
     * @return the column type
     */
    public Type<T> getType() {
        return mColumnType;
    }

    /**
     * Returns a string composed as follow TABLE.COLUMN
     * @return the table name, a dot and the column name
     */
    public String getTableDotName() {
        String tableNameDotColumnName = "";
        if (mTable != null)
            tableNameDotColumnName += mTable + ".";
        tableNameDotColumnName += mColumnName;
        return tableNameDotColumnName;
    }

    /**
     * Returns true if the table name, the column name and the column type are not null.
     * @return whether the column is well defined
     */
    public boolean isWellDefined() {
        return mTable != null && mColumnName != null && mColumnType != null;
    }

    /**
     * Returns the DDL string for this column.
     * <p>
     * e.g. USERNAME PRIMARY KEY AUTO_INCREMENT
     * @return the definition of this column
     */
    public String getColumnDefinition() {
        String colStr = mColumnName + " " + mColumnType.toSql();
        if (isPrimaryKey())
            colStr += " PRIMARY KEY";
        if (getNotNull())
            colStr += " NOT NULL";
        if (isAutoIncrement())
            colStr += " AUTO_INCREMENT";
        if (hasDefaultValue())
            colStr += " DEFAULT ?";
        // Inline FK not implemented since MYSQL doesn't support it
        return colStr;
    }


    @Override
    public String toSql() {
        return getTableDotName();
    }


    public List<Object> getDDLBindableObjects() {
        List<Object> objs = null;
        if (mDefault != null) {
            objs = new ArrayList<>();
            objs.add(mDefault);
        }
        return objs;
    }

    // -------------------------------------------------------------------------
    // -------------------------------- DQL ------------------------------------
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // ---------------------------- ARITHMETIC ---------------------------------
    // -------------------------------------------------------------------------

    // +

    public Expression add(Object o) { return Operators.add(this, o); }

    // -

    public Expression sub(Object o) { return Operators.sub(this, o); }

    // *

    public Expression mul(Object o) { return Operators.mul(this, o); }

    // /

    public Expression div(Object o) { return Operators.div(this, o); }

    // %

    public Expression mod(Object o) { return Operators.mod(this, o); }


    // -------------------------------------------------------------------------
    // ---------------------------- COMPARISON ---------------------------------
    // -------------------------------------------------------------------------

    // =

    public Expression eq(Object o) { return Operators.eq(this, o); }

    // <>

    public Expression neq(Object o) {
        return Operators.neq(this, o);
    }

    // >

    public Expression gt(Object o) {
        return Operators.gt(this, o);
    }

    // >=

    public Expression ge(Object o) {
        return Operators.ge(this, o);
    }

    // <

    public Expression lt(Object o) { return Operators.lt(this, o); }

    // <=

    public Expression le(Object o) {
        return Operators.le(this, o);
    }

    // -------------------------------------------------------------------------
    // ----------------------------- LOGICAL -----------------------------------
    // -------------------------------------------------------------------------

    // AND

    public Expression and(Object o) {
        return Operators.and(this, o);
    }

    // OR

    public Expression or(Object o) {
        return Operators.or(this, o);
    }

    // XOR

    public Expression xor(Object o) {
        return Operators.xor(this, o);
    }

    // LIKE

    public Expression like(Object o) { return Operators.like(this, o); }

    // BETWEEN

    public Expression between(Object o1, Object o2) { return Operators.between(this, o1, o2); }

    // IS NULL

    public Expression isNull() { return Operators.isNull(this); }

    // IS NOT NULL

    public Expression isNotNull() { return Operators.isNotNull(this); }

    // -------------------------------------------------------------------------
    // ----------------------------- BITWISE -----------------------------------
    // -------------------------------------------------------------------------

    // &

    public Expression bitAnd(Object o) {
        return Operators.bitAnd(this, o);
    }

    // |

    public Expression bitOr(Object o) {
        return Operators.bitOr(this, o);
    }

    // ^

    public Expression bitXor(Object o) {
        return Operators.bitXor(this, o);
    }

    // -------------------------------------------------------------------------
    // --------------------------- SUB QUERY -----------------------------------
    // -------------------------------------------------------------------------

    // IN

    public Expression in(Select expr) {
        return Operators.in(this, expr);
    }

    // NOT IN

    public Expression notIn(Select expr) {
        return Operators.notIn(this, expr);
    }


    public List<Object> getDQLBindableObjects() {
        return getBindableObjects();
    }

    @Override
    public List<Object> getBindableObjects() {
        return null;
    }
}
