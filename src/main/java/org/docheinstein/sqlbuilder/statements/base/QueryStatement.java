package org.docheinstein.sqlbuilder.statements.base;

import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.models.Column;
import org.docheinstein.sqlbuilder.models.Tuple;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Represents a query statement (actually only the SELECT is a query statement)
 * for retrieve data from the database.
 *
 * <p>
 * This class could have been merged into SELECT but I preferred keep the things
 * separated since this class' methods work on the result set, while
 * {@link org.docheinstein.sqlbuilder.statements.shared.Select}
 * contains the stuff related to the syntax of the statement.
 */
public interface QueryStatement extends Statement {

    /**
     * Executes this statement over the specified connection.
     * @param connection the connection
     * @return the result set obtained by executing this statement
     * @throws SQLException if the query fails
     */
    default ResultSet exec(Connection connection) throws SQLException {
        if (connection == null)
            return null;

        return SqlBuilderInternalUtil.boundStatement(
            SqlBuilderInternalUtil.getStatement(
                connection, toSql()), getBindableObjects()
        ).executeQuery();
    }

    /**
     * Executes this statement over the specified connection using a previously
     * cached {@link java.sql.PreparedStatement} if it exists or creating a new
     * one if it doesn't.
     * @param connection the connection
     * @param identifier the cache identifier of this statement
     * @return the result set obtained by executing this statement
     * @throws SQLException if the query fails
     */
    default ResultSet execCache(Connection connection, int identifier) throws SQLException {
        if (connection == null)
            return null;

        return SqlBuilderInternalUtil.execQuery(connection, this, identifier);
    }

    /**
     * Retrieves the tuples that satisfy this query.
     * <p>
     * The specified class must be a valid {@link Tuple} and must have the
     * field to retrieve annotated with
     * {@link org.docheinstein.sqlbuilder.models.ColumnField}.
     *
     * @param connection the connection
     * @param clazz the class of the tuples to retrieve
     * @param <T> the type of the tuples to retrieve
     * @return a list of tuples that satisfy this query
     * @throws SQLException if the query fails
     */
    default <T extends Tuple> List<T> fetch(Connection connection, Class<T> clazz)
        throws SQLException {
        List<T> list = new ArrayList<>();
        forEach(connection, clazz, list::add);
        return list;
    }

    /**
     * Retrieves the tuples that satisfy this query using a previously
     * cached {@link java.sql.PreparedStatement} if it exists or creating a new
     * one if it doesn't.
     * <p>
     * The specified class must be a valid {@link Tuple} and must have the
     * field to retrieve annotated with
     * {@link org.docheinstein.sqlbuilder.models.ColumnField}.
     *
     * @param connection the connection
     * @param clazz the class of the tuples to retrieve
     * @param identifier the cache identifier of this statement
     * @param <T> the type of the tuples to retrieve
     * @return a list of tuples that satisfy this query
     * @throws SQLException if the query fails
     */
    default <T extends Tuple> List<T> fetchCache(Connection connection, Class<T> clazz,
                                                int identifier) throws SQLException {
        List<T> list = new ArrayList<>();
        forEachCache(connection, clazz, list::add, identifier);
        return list;
    }

    /**
     * Performs an action for each tuple that satisfy this query.
     * @param connection the connection
     * @param clazz the class of the tuples on which perform the action
     * @param resultSetConsumer the consumer that performs an action over each tuple
     * @param <T> the type of the tuples to retrieve
     * @throws SQLException if the query fails
     */
    default <T extends Tuple> void forEach(Connection connection, Class<T> clazz,
                                          Consumer<T> resultSetConsumer)
        throws SQLException {
        forEachInternal(clazz, resultSetConsumer, exec(connection));
    }

    /**
     * Performs an action for each tuple that satisfy this query using a previously
     * cached {@link java.sql.PreparedStatement} if it exists or creating a new
     * one if it doesn't.
     * @param connection the connection
     * @param clazz the class of the tuples on which perform the action
     * @param resultSetConsumer the consumer that performs an action over each tuple
     * @param identifier the cache identifier of this statement
     * @param <T> the type of the tuples to retrieve
     * @throws SQLException if the query fails
     */
    default <T extends Tuple> void forEachCache(Connection connection, Class<T> clazz,
                                               Consumer<T> resultSetConsumer, int identifier)
        throws SQLException {
        forEachInternal(clazz, resultSetConsumer, execCache(connection, identifier));
    }


    /**
     * Performs an action for each tuple of the given result set
     * @param clazz the class of the tuples on which perform the action
     * @param resultSetConsumer the consumer that performs an action over each tuple
     * @param resultSet the result set from which retrieve the tuples
     * @param <T> the type of the tuples to retrieve
     * @throws SQLException if the query fails
     */
    default <T extends Tuple> void forEachInternal(Class<T> clazz,
                                                   Consumer<T> resultSetConsumer,
                                                   ResultSet resultSet) throws SQLException {

        try {
            // Retrieves the map that associated the column to the tuple's fields
            Map<String, Field> columnFieldMap = SqlBuilderInternalUtil.getColumnFields(clazz);

            // For each row of the result set and for each ColumnField of the tuple
            // tries to grab the value from the result set of the column
            // that matches the name of the ColumnField
            while (resultSet.next()) {
                T t = clazz.newInstance();
                for (Column column : getColumns()) {
                    if (!column.isWellDefined())
                        throw new RuntimeException("Can't fetch a string column, provide a valid one");
                    String colName = column.getName();
                    Field field = columnFieldMap.get(colName);
                    if (field != null) {
                        Object o = column.getType().getFromResultSet(
                            resultSet, column.getTableDotName());
                        String oStr = o != null ? o.toString() : "null";
                        SqlBuilderLogger.out("Filling field [" + colName + "] with [" + oStr + "]");
                        field.set(t, o);
                    }
                }
                resultSetConsumer.accept(t);
            }
        }
        catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("Fetch failed for type: " + clazz.getName());
        }
    }

    /**
     * Returns the columns this query should work on.
     * @return the columns of this query
     */
    List<Column> getColumns();
}
