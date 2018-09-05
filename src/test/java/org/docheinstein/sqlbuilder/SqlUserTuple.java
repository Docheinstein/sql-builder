package org.docheinstein.sqlbuilder;

import org.docheinstein.sqlbuilder.models.ColumnField;
import org.docheinstein.sqlbuilder.models.Tuple;

public class SqlUserTuple implements Tuple {
    @ColumnField(SqlUserTable.USERNAME)
    private String mUsername;

    @ColumnField(SqlUserTable.PASSWORD)
    private String mPassword;

    @ColumnField(SqlUserTable.CAPABILITIES)
    private int mCapabilitiesRaw = 0;
}
