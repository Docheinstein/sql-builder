package com.docheinstein.sqlbuilder.example;

import com.docheinstein.sqlbuilder.annotations.ColumnField;
import com.docheinstein.sqlbuilder.models.Tuple;

public class SqlUserTuple implements Tuple {
    @ColumnField(SqlUserTable.USERNAME)
    private String mUsername;

    @ColumnField(SqlUserTable.PASSWORD)
    private String mPassword;

    @ColumnField(SqlUserTable.CAPABILITIES)
    private int mCapabilitiesRaw = 0;
}
