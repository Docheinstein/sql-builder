package com.docheinstein.sqlbuilder.statements;

import com.docheinstein.sqlbuilder.models.Column;
import com.docheinstein.sqlbuilder.models.Table;

import java.util.List;

public class Statements {

    public static Create create(Table table) {
        return new Create(table);
    }

    public static Delete delete(Table table) {
        return new Delete(table);
    }

    public static Drop drop(Table table) { return new Drop(table); }

    public static Insert insert(Table table) {
        return new Insert(table);
    }

    public static Replace replace(Table table) {
        return new Replace(table);
    }

    public static Select select(String... columns) {
        return new Select(columns);
    }

    public static Select select(Column... columns) {
        return new Select(columns);
    }

    public static Select select(List<Column> columns) {
        return new Select(columns);
    }

    public static Update update(Table table) { return new Update(table); }

    public static Alter alter(Table table) {
        return new Alter(table);
    }
}
