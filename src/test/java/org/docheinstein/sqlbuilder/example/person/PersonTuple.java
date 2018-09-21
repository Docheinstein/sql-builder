package org.docheinstein.sqlbuilder.example.person;

import org.docheinstein.sqlbuilder.models.ColumnField;
import org.docheinstein.sqlbuilder.models.Tuple;

public class PersonTuple implements Tuple {

    @ColumnField(PersonTable.ID_COL_NAME)
    public Integer id;

    @ColumnField(PersonTable.NAME_COL_NAME)
    public String name;

    @ColumnField(PersonTable.SURNAME_COL_NAME)
    public String surname;

    @ColumnField(PersonTable.AGE_COL_NAME)
    public Integer age;

    @Override
    public String toString() {
        return id + " | " + name + " | " + surname + " | " + age;
    }
}
