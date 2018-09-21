package org.docheinstein.sqlbuilder.example.dog;


import org.docheinstein.sqlbuilder.clauses.ForeignKey;
import org.docheinstein.sqlbuilder.example.person.PersonTable;
import org.docheinstein.sqlbuilder.models.Column;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.types.Int;
import org.docheinstein.sqlbuilder.types.Varchar;

public class DogTable extends Table {

    private static final String TABLE_NAME =           "Dog";

    // Columns name

    public static final String OWNER_COL_NAME =        "Owner";
    public static final String NUMBER_COL_NAME =       "Number";
    public static final String NICKNAME_COL_NAME =     "NickName";

    // Columns

    public static Column<Integer> OWNER =
        new Column<>(TABLE_NAME, OWNER_COL_NAME, new Int());

    public static Column<Integer> NUMBER =
        new Column<>(TABLE_NAME, NUMBER_COL_NAME, new Int());

    public static Column<String> NICKNAME =
        new Column<>(TABLE_NAME, NICKNAME_COL_NAME, new Varchar(32))
            .notNull();

    public DogTable() {
        super(TABLE_NAME);

        // Adds the columns to the table within the constructor
        col(OWNER);
        col(NUMBER);
        col(NICKNAME);

        // Composed primary key
        primaryKey(OWNER_COL_NAME, NUMBER_COL_NAME);

        // Foreign key
        foreignKey(
            OWNER,
            PersonTable.ID,
            ForeignKey.ReferenceAction.Cascade,
            ForeignKey.ReferenceAction.Cascade
        );

        // Eventually add CHECK constraint
        check(NUMBER.gt(0)); // Number of dog > 0
    }
}

