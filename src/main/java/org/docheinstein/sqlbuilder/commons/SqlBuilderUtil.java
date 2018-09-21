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

/**
 * Contains a bunch of method used frequently by end users.
 */
public class SqlBuilderUtil {

    // -------------------------------------------------------------------------
    // ---------------------------- EXISTENCE ----------------------------------
    // -------------------------------------------------------------------------

    /**
     * Returns true if the given table exists.
     * @param table the table
     * @param connection the connection
     * @return whether the table exists
     */
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

    /**
     * Returns true if the given column exists in its table
     * @param column the table
     * @param connection the connection
     * @return  whether the table exists
     */
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
    // -------------------------------------------------------------------------
    // ------------------------------ SELECT -----------------------------------
    // -------------------------------------------------------------------------

    /**
     * Returns the COUNT(*) of a table.
     * @param table the table
     * @param connection the connection
     * @return the number of rows of the table
     * @throws SQLException if the query fails
     */
    public static int getCount(Table table, Connection connection) throws SQLException {
        return getCount(table, null, connection);
    }

    /**
     * Returns the COUNT(*) of a table using the given WHERE expression
     * @param table the table
     * @param whereExpression an optional where expression
     * @param connection the connection
     * @return the number of rows of the table for the given where expression
     * @throws SQLException  if the query fails
     */
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

    /**
     * Returns the first tuple of the table using the given SELECT statement.
     * @param select the select statement
     * @param tupleClass the class of the tuple (must be passed because of type erasure)
     * @param connection the connection
     * @param <T> the type of the tuple
     * @return the first tuple of the column for the given SELECT statement
     * @throws SQLException  if the query fails
     */
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

    /**
     * Returns the first tuple of the table using the given SELECT statement
     * which will be retrieved from the cache if has already been used.
     * @param select the select statement
     * @param tupleClass the class of the tuple (must be passed because of type erasure)
     * @param connection the connection
     * @param cacheIdentifier the unique identifier of the statement
     * @param <T> the type of the tuple
     * @return the first tuple of the column for the given SELECT statement
     * @throws SQLException  if the query fails
     */
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
