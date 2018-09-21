package org.docheinstein.sqlbuilder.models;

import org.docheinstein.sqlbuilder.SqlBindable;
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
        // Inline FK not implemented since MYSQL doesn't support it
        return colStr;
    }


    @Override
    public String toSql() {
        return getTableDotName();
    }

    @Override
    public List<Object> getBindableObjects() {
        return new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // -------------------------------- DQL ------------------------------------
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // ---------------------------- ARITHMETIC ---------------------------------
    // -------------------------------------------------------------------------

    // +

    public Expression add(Column<T> column) { return new Operators.Add(this, column); }

    public Expression add(T value) {
        return new Operators.Add(this, value);
    }

    public Expression add(Expression expr) { return new Operators.Add(this, expr); }

    // -

    public Expression sub(Column<T> column) { return new Operators.Sub(this, column); }

    public Expression sub(T value) {
        return new Operators.Sub(this, value);
    }

    public Expression sub(Expression expr) { return new Operators.Sub(this, expr); }

    // *

    public Expression mul(Column<T> column) { return new Operators.Mul(this, column); }

    public Expression mul(T value) {
        return new Operators.Mul(this, value);
    }

    public Expression mul(Expression expr) { return new Operators.Mul(this, expr); }

    // /

    public Expression div(Column<T> column) { return new Operators.Div(this, column); }

    public Expression div(T value) {
        return new Operators.Div(this, value);
    }

    public Expression div(Expression expr) { return new Operators.Div(this, expr); }

    // %

    public Expression mod(Column<T> column) { return new Operators.Mod(this, column); }

    public Expression mod(T value) {
        return new Operators.Mod(this, value);
    }

    public Expression mod(Expression expr) { return new Operators.Mod(this, expr); }

    // -------------------------------------------------------------------------
    // ---------------------------- COMPARISON ---------------------------------
    // -------------------------------------------------------------------------

    // =

    public Expression eq(Column<T> column) { return new Operators.Eq(this, column); }

    public Expression eq(T value) {
        return new Operators.Eq(this, value);
    }

    public Expression eq(Expression expr) { return new Operators.Eq(this, expr); }

    // <>

    public Expression neq(Column<T> column) {
        return new Operators.Neq(this, column);
    }

    public Expression neq(T value) {
        return new Operators.Neq(this, value);
    }

    public Expression neq(Expression expr) { return new Operators.Neq(this, expr); }

    // >

    public Expression gt(Column<T> column) {
        return new Operators.Gt(this, column);
    }

    public Expression gt(T value) {
        return new Operators.Gt(this, value);
    }

    public Expression gt(Expression expr) {
        return new Operators.Gt(this, expr);
    }

    // >=

    public Expression ge(Column<T> column) {
        return new Operators.Ge(this, column);
    }

    public Expression ge(T value) {
        return new Operators.Ge(this, value);
    }

    public Expression ge(Expression expr) {
        return new Operators.Ge(this, expr);
    }

    // <

    public Expression lt(Column<T> column) { return new Operators.Lt(this, column); }

    public Expression lt(T value) {
        return new Operators.Lt(this, value);
    }

    public Expression lt(Expression expr) {
        return new Operators.Lt(this, expr);
    }

    // <=

    public Expression le(Column<T> column) {
        return new Operators.Le(this, column);
    }

    public Expression le(T value) {
        return new Operators.Le(this, value);
    }

    public Expression le(Expression expr) {
        return new Operators.Le(this, expr);
    }

    // -------------------------------------------------------------------------
    // ----------------------------- LOGICAL -----------------------------------
    // -------------------------------------------------------------------------

    // AND

    public Expression and(Column<T> column) {
        return new Operators.And(this, column);
    }

    public Expression and(T value) { return new Operators.And(this, value); }

    public Expression and(Expression expr) { return new Operators.And(this, expr); }

    // OR

    public Expression or(Column<T> column) {
        return new Operators.Or(this, column);
    }

    public Expression or(T value) { return new Operators.Or(this, value); }

    public Expression or(Expression expr) { return new Operators.Or(this, expr); }

    // XOR

    public Expression xor(Column<T> column) {
        return new Operators.Xor(this, column);
    }

    public Expression xor(T value) {
        return new Operators.Xor(this, value);
    }

    public Expression xor(Expression expr) { return new Operators.Xor(this, expr); }

    // LIKE

    public Expression like(Column<T> column) { return new Operators.Like(this, column); }

    public Expression like(T value) { return new Operators.Like(this, value); }

    public Expression like(Expression e) { return new Operators.Like(this, e); }

    // BETWEEN

    public Expression between(Expression e1, Expression e2) { return new Operators.Between(this, e1, e2); }

    public Expression between(Expression e, Column c) { return new Operators.Between(this, e, c); }

    public Expression between(Expression e, Object v) { return new Operators.Between(this, e, v); }

    public Expression between(Column<T> c1, Column<T> c2) { return new Operators.Between(this, c1, c2); }

    public Expression between(Column<T> c, Expression e) { return new Operators.Between(this, c, e); }

    public Expression between(Column<T> c, T v) { return new Operators.Between(this, c, v); }

    public Expression between(T v1, T v2) { return new Operators.Between(this, v1, v2); }

    public Expression between(Object v, Expression e) { return new Operators.Between(this, v, e); }

    // IS NULL

    public Expression isNull() { return new Operators.IsNull(this); }

    // IS NOT NULL

    public Expression isNotNull() { return new Operators.IsNotNull(this); }

    // -------------------------------------------------------------------------
    // ----------------------------- BITWISE -----------------------------------
    // -------------------------------------------------------------------------

    // &

    public Expression bitAnd(Column<T> column) {
        return new Operators.BitAnd(this, column);
    }

    public Expression bitAnd(T value) {
        return new Operators.BitAnd(this, value);
    }

    public Expression bitAnd(Expression expr) {
        return new Operators.BitAnd(this, expr);
    }

    // |

    public Expression bitOr(Column<T> column) {
        return new Operators.BitOr(this, column);
    }

    public Expression bitOr(T value) {
        return new Operators.BitOr(this, value);
    }

    public Expression bitOr(Expression expr) {
        return new Operators.BitOr(this, expr);
    }

    // ^

    public Expression bitXor(Column<T> column) {
        return new Operators.BitXor(this, column);
    }

    public Expression bitXor(T value) {
        return new Operators.BitXor(this, value);
    }

    public Expression bitXor(Expression expr) {
        return new Operators.BitXor(this, expr);
    }

    // -------------------------------------------------------------------------
    // --------------------------- SUB QUERY -----------------------------------
    // -------------------------------------------------------------------------

    // IN

    public Expression in(Select expr) {
        return new Operators.In(this, expr);
    }

    // NOT IN

    public Expression notIn(Select expr) {
        return new Operators.NotIn(this, expr);
    }
}
