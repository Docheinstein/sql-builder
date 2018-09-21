package org.docheinstein.sqlbuilder.example.person;

import org.docheinstein.sqlbuilder.models.Column;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.types.Int;
import org.docheinstein.sqlbuilder.types.Varchar;

public class PersonTable extends Table {

    private static final String TABLE_NAME =        "Person";

    // Columns name

    public static final String ID_COL_NAME =        "Id";
    public static final String NAME_COL_NAME =      "Name";
    public static final String SURNAME_COL_NAME =   "Surname";
    public static final String AGE_COL_NAME =       "Age";

    // Columns

    public static Column<Integer> ID =
        new Column<>(TABLE_NAME, ID_COL_NAME, new Int())
        .autoIncrement() // AUTO_INCREMENT
        .primaryKey();   // PRIMARY KEY

    public static Column<String> NAME =
        new Column<>(TABLE_NAME, NAME_COL_NAME, new Varchar(32))
        .notNull();

    public static Column<String> SURNAME =
        new Column<>(TABLE_NAME, SURNAME_COL_NAME, new Varchar(32))
        .notNull();

    public static Column<Integer> AGE =
        new Column<>(TABLE_NAME, AGE_COL_NAME, new Int());


    public PersonTable() {
        super(TABLE_NAME);

        // Add the columns to the table
        col(ID);
        col(NAME);
        col(SURNAME);
        col(AGE);

        // Eventually add the CHECK constraint
        check(AGE.gt(0)); // Age > 0
    }
}
