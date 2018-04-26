package com.docheinstein.sqlbuilder.example;

import com.docheinstein.sqlbuilder.models.Column;
import com.docheinstein.sqlbuilder.models.Table;
import com.docheinstein.sqlbuilder.types.Int;
import com.docheinstein.sqlbuilder.types.Varchar;

public class SqlUserTable extends Table {
    public static final String TABLE_NAME = "User";

    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";
    public static final String CAPABILITIES = "Capabilities";

    public Column<String> username =
        new Column<>(this, USERNAME, new Varchar(64))
            .primaryKey();
    public Column<String> password =
        new Column<>(this, PASSWORD, new Varchar(64));
    public Column<Integer> capabilities =
        new Column<>(this, CAPABILITIES, new Int());

    SqlUserTable() {
        super(TABLE_NAME);
        col(username);
        col(password);
        col(capabilities);
    }
}
