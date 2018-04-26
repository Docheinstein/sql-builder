package org.docheinstein.sqlbuilder.models;

import org.docheinstein.sqlbuilder.clauses.ForeignKey;
import org.docheinstein.sqlbuilder.expressions.Expression;
import org.docheinstein.sqlbuilder.statements.*;
import org.docheinstein.sqlbuilder.types.base.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table {
    private String mName;
    private List<Column> mColumns = new ArrayList<>();
    private List<String> mPrimaryKey = new ArrayList<>();
    private List<ForeignKey> mForeignKeys = new ArrayList<>();
    private Expression mCheck;

    public Table(String name) {
        mName = name;
    }

    public <T> Table col(String name, Type<T> type) {
        return col(new Column<>(this, name, type));
    }

    public Table col(Column col) {
        mColumns.add(col);
        return this;
    }

    public Table primaryKey(String... columns) {
        mPrimaryKey.addAll(Arrays.asList(columns));
        return this;
    }

    public Table primaryKey(Column... columns) {
        for (Column c : columns)
            mPrimaryKey.add(c.getName());
        return this;
    }

//    public Table foreignKey

    public Table addForeignKey(ForeignKey fk) {
        mForeignKeys.add(fk);
        return this;
    }

    public <T> Table foreignKey(Column<T> internalCol, Column<T> externalCol,
                               ForeignKey.ReferenceOption onDelete,
                               ForeignKey.ReferenceOption onUpdate) {
        return addForeignKey(new ForeignKey<>(internalCol, externalCol, onDelete, onUpdate));
    }

    public <T> Table foreignKey(Column<T> internalCol, Column<T> externalCol) {
        return addForeignKey(new ForeignKey<>(internalCol, externalCol, null, null));
    }

    public Table check(Expression e) {
        mCheck = e;
        return this;
    }

    public String getName() {
        return mName;
    }

    public List<Column> getColumns() {
        return mColumns;
    }

    public List<String> getPrimaryKey() {
        return mPrimaryKey;
    }

    public List<ForeignKey> getForeignKeys() {
        return mForeignKeys;
    }

    public Expression getCheck() {
        return mCheck;
    }

    // ==================
    // === STATEMENTS ===
    // ==================

    public Create create() {
        return Statements.create(this);
    }

    public Delete delete() {
        return Statements.delete(this);
    }

    public Drop drop() {
        return Statements.drop(this);
    }

    public Insert insert() {
        return Statements.insert(this);
    }

    public Replace replace() {
        return Statements.replace(this);
    }

    public Select select(String... columns) {
        return Statements.select(columns).from(this);
    }

    public Select select(Column... columns) {
        return Statements.select(columns).from(this);
    }

    public Select select(List<Column> columns) {
        return Statements.select(columns).from(this);
    }

    public Update update() {
        return Statements.update(this);
    }

    public Alter alter() { return Statements.alter(this); }
}
