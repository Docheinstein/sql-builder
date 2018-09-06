package org.docheinstein.sqlbuilder.types;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Bool extends Type<Boolean> {

    @Override
    public String toSql() {
        return "TINYINT(1)";
    }

    @Override
    public Boolean getFromResultSet(ResultSet resultSet, String columnName) throws SQLException {
        // resultSet.getBoolean() returns false even if the value was null;
        // instead we returns a null value if the result was null
        boolean fromResultSet = resultSet.getBoolean(columnName);
        return resultSet.wasNull() ? null : fromResultSet;
    }
}
