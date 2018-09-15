package org.docheinstein.sqlbuilder.commons;

import org.docheinstein.sqlbuilder.expressions.Expression;
import org.docheinstein.sqlbuilder.models.Column;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.models.Tuple;
import org.docheinstein.sqlbuilder.statements.shared.Select;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SqlBuilderUtil {

    // ---------------------- EXISTENCE -----------------------

    public static boolean checkExistence(Table table, Connection connection) {
        if (table == null || connection == null)
            throw new InvalidParameterException("Table and connection must be not null");

        try {
            table.select(table.getColumns()).exec(connection);
            return true;  // Can SELECT from TABLE => exists
        } catch (SQLException e) {
            return false; // Can't SELECT from TABLE => doesn't exist
        }
    }

    public static boolean checkExistence(Column column, Connection connection) {
        if (column == null || connection == null)
            throw new InvalidParameterException("Column and connection must be not null");

        try {
            ResultSet rs = connection.getMetaData().getColumns(
                null, null,
                column.getTable(), column.getName());
            return rs != null && rs.first();
        } catch (SQLException e) {
            return false;
        }
    }

    // ---------------------- SELECT -----------------------

    public static int getCount(Table table, Connection connection) throws SQLException {
        return getCount(table, null, connection);
    }

    public static int getCount(Table table, Expression whereExpression,
                               Connection connection) throws SQLException {
        if (table == null || connection == null)
            throw new InvalidParameterException("Table and connection must be not null");

        Select selectStatement = table.select("COUNT(*) as count");

        if (whereExpression != null)
            selectStatement = selectStatement.where(whereExpression);

        ResultSet rs = selectStatement.exec(connection);
        if (rs.next())
            return rs.getInt("count");
        return 0;
    }

    public static <T extends Tuple> T fetchFirst(Select select, Class<T> tupleClass,
                                               Connection connection)
        throws SQLException {
        if (select == null || connection == null)
            throw new InvalidParameterException("Select and connection must be not null");

        List<T> ts = select
            .fetch(
                connection,
                tupleClass
            );

        if (ts.size() > 0)
            return ts.get(0);

        return null;

    }

    public static <T extends Tuple> T fetchFirstCache(Select select, Class<T> tupleClass,
                                                      Connection connection, int cacheIdentifier)
        throws SQLException {
        if (select == null || connection == null)
            throw new InvalidParameterException("Select and connection must be not null");


        List<T> ts = select
            .fetchCache(
                connection,
                tupleClass,
                cacheIdentifier
            );

        if (ts.size() > 0)
            return ts.get(0);

        return null;
    }

}
