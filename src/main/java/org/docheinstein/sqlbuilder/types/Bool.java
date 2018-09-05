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
        return resultSet.getBoolean(columnName);
    }
}
