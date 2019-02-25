package org.docheinstein.sqlbuilder.commons;

import org.docheinstein.sqlbuilder.SqlBindable;
import org.docheinstein.sqlbuilder.Sqlable;
import org.docheinstein.sqlbuilder.exceptions.UnsupportedSqlLanguageException;
import org.docheinstein.sqlbuilder.expressions.Expression;
import org.docheinstein.sqlbuilder.models.Tuple;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;

/**
 * Contains methods used by the library for create, cache and execute statements.
 */
public class SqlBuilderInternalUtil {

    // -------------------------------------------------------------------------
    // -------------------------- SQLBUILDER GENERAL ---------------------------
    // -------------------------------------------------------------------------

    /**
     * Throws an {@link UnsupportedSqlLanguageException} if the current library language
     * doesn't match the parameter.
     * @param sqlLanguage the expected sql language
     */
    public static void throwIfCurrentLanguageIsNot(SqlLanguage sqlLanguage) {
        if (!SqlBuilder.isLanguageSet() || sqlLanguage != SqlBuilder.getLanguage())
            throw new UnsupportedSqlLanguageException();
    }

    /**
     * Throws an {@link UnsupportedSqlLanguageException} if the current library language
     * matches the parameter.
     * @param sqlLanguage the unexpected sql language
     */
    public static void throwIfCurrentLanguageIs(SqlLanguage sqlLanguage) {
        if (!SqlBuilder.isLanguageSet() || sqlLanguage == SqlBuilder.getLanguage())
            throw new UnsupportedSqlLanguageException();
    }

    // -------------------------------------------------------------------------
    // ------------------------- SQL STRING BUILDING ---------------------------
    // -------------------------------------------------------------------------

    /**
     * Scans an iterable collection of elements and for each of those
     * calls applies the string converter method of
     * the given {@link Function} interface.
     * The value is then appended to the list, separated with a comma.
     * @param elements the elements to scan
     * @param stringConverter the interface that defines the rules
     *                        to use convert an object to a string before
     *                        add it to the list
     * @param <T> any type
     * @return  a string created with by appending each element
     *          with a comma.
     *          e.g: "Username, Password"
     *
     * @see #getAsStringList(Iterable)
     */
    public static <T> String getAsCommaList(Iterable<T> elements,
                                            Function<T, String> stringConverter) {
        StringJoiner joiner = new StringJoiner(", ");
        elements.forEach(el -> joiner.add(stringConverter.apply(el)));
        return joiner.toString();
    }

    /**
     * Returns a string created by appending each string separated with a comma.
     * @param strings the strings
     * @return a string created by appending each string separated with a comma
     *         e.g: "Username, Password"
     *
     * @see #getAsCommaList(Iterable, Function)
     */
    public static String getAsStringList(Iterable<String> strings) {
        return getAsCommaList(strings, s -> s);
    }

    /**
     * Returns a string with the format "expressionName (expr_sql)".
     * <p>
     * e.g. WHERE (expr)
     * @param expr the expression
     * @param expressionName the name of the expression
     * @return a string with the format "expressionName (expr_sql)"
     */
    public static String getNamedExpressionAsString(Expression expr,
                                                    String expressionName) {
        return
            expr == null ? "" :
            " " + expressionName + " (" + expr.toSql() + ")";
    }

    // -------------------------------------------------------------------------
    // ---------------- STATEMENTS AND COLUMN FIELDS RETRIEVING ----------------
    // -------------------------------------------------------------------------


    /**
     * Returns the column field map for the given class which contains all the fields
     * marked with {@link org.docheinstein.sqlbuilder.models.ColumnField}.
     * <p>
     * Actually this method retrieves the column field map from the cache if exists.
     * If it doesn't exist a new column field map is created and then cached for
     * further uses.
     *
     * @param clazz the class
     * @param <T> the type of the class
     * @return the column field map for the given class
     * @throws IllegalAccessException if the class in inaccessible
     * @throws InstantiationException if the class can't be instantiated
     */
    public static <T extends Tuple> Map<String, Field> getColumnFields(Class<T> clazz)
        throws IllegalAccessException, InstantiationException {

        Map<String, Field> columnFieldMap = SqlBuilderCache.Tuples.get(clazz);

        if (columnFieldMap == null) {
            // Creates a new map and caches it
            columnFieldMap = clazz.newInstance().getColumnFieldMap();
            SqlBuilderCache.Tuples.put(clazz, columnFieldMap);
        }

        return columnFieldMap;
    }

    /**
     * Returns the {@link PreparedStatement} associated with the given sql string.
     * <p>
     * Actually this method retrieves the statement from the cache if exists.
     * If it doesn't exist, a new statement is created and then cached for
     * further uses.
     *
     * @param connection the connection
     * @param sql the sql string
     * @return the statement associated with the given sql string
     * @throws SQLException if the sql string contains errors
     *
     * @see #getStatement(Connection, String, boolean)
     * @see #getStatement(Connection, Sqlable, int)
     * @see #getStatement(Connection, Sqlable, int, boolean)
     */
    public static PreparedStatement getStatement(Connection connection, String sql)
        throws SQLException {

        return getStatement(connection, sql, false);
    }

    /**
     * Returns the {@link PreparedStatement} associated with the given sql string
     * and optionally enables the RETURN_GENERATED_KEYS for the statement
     * which can be used for retrieve an auto increment value of the tuple.
     * <p>
     * Actually this method retrieves the statement from the cache if exists.
     * If it doesn't exist, a new statement is created and then cached for
     * further uses.
     *
     * @param connection the connection
     * @param sql the sql string
     * @param returnGeneratedKeys whether the RETURN_GENERATED_KEYS flag should
     *                            be passed for the creation of the statement
     * @return the statement associated with the given sql string
     * @throws SQLException if the sql string contains errors
     *
     * @see #getStatement(Connection, String)
     * @see #getStatement(Connection, Sqlable, int)
     * @see #getStatement(Connection, Sqlable, int, boolean)
     */
    public static PreparedStatement getStatement(
        Connection connection, String sql, boolean returnGeneratedKeys)
        throws SQLException {

        PreparedStatement statement = SqlBuilderCache.Statements.get(sql);

        if (statement != null && !statement.isClosed()) {
            SqlBuilderLogger.out("Reusing cached statement for SQL {" + sql + "}");
            return statement;
        }

        // Creates a new statement and caches it

        SqlBuilderLogger.out(
            statement == null ?
                "First creation of statement for SQL {" + sql + "}" :
                "Recreating statement since cached one is closed for SQL {" + sql + "}"
        );

        statement = returnGeneratedKeys ?
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) :
            connection.prepareStatement(sql);

        SqlBuilderCache.Statements.put(sql, statement);

        return statement;
    }

    /**
     * Returns the {@link PreparedStatement} associated with the given sqlable entity.
     * <p>
     * Actually this method retrieves the statement from the cache if exists.
     * If it doesn't exist, a new statement is created and then cached for
     * further uses.
     *
     * @param connection the connection
     * @param sqlable the sqlable entity
     * @param identifier the cache's identifier of the sqlable entity
     * @return the statement associated with the given sqlable entity
     * @throws SQLException if the sql string contains errors
     *
     * @see #getStatement(Connection, String)
     * @see #getStatement(Connection, String, boolean)
     * @see #getStatement(Connection, Sqlable, int, boolean)
     */
    public static PreparedStatement getStatement(
        Connection connection, Sqlable sqlable, int identifier) throws SQLException {
        return getStatement(connection, sqlable, identifier, false);
    }

    /**
     * Returns the {@link PreparedStatement} associated with the given sqlable entity
     * and optionally enables the RETURN_GENERATED_KEYS for the statement
     * which can be used for retrieve an auto increment value of the tuple.
     * <p>
     * Actually this method retrieves the statement from the cache if exists.
     * If it doesn't exist, a new statement is created and then cached for
     * further uses.
     *
     * @param connection the connection
     * @param sqlable the sqlable entity
     * @param identifier the cache's identifier of the sqlable entity
     * @param returnGeneratedKeys whether the RETURN_GENERATED_KEYS flag should
     *                            be passed for the creation of the statement
     * @return the statement associated with the given sqlable entity
     * @throws SQLException if the sql string contains errors
     *
     * @see #getStatement(Connection, String)
     * @see #getStatement(Connection, String, boolean)
     * @see #getStatement(Connection, Sqlable, int)
     */
    public static PreparedStatement getStatement(
        Connection connection, Sqlable sqlable,
        int identifier, boolean returnGeneratedKeys) throws SQLException {

        PreparedStatement statement = SqlBuilderCache.Statements.get(identifier);

        if (statement != null && !statement.isClosed()) {
            SqlBuilderLogger.out("Reusing cached statement for SQL statement [ID = " + identifier + "]");
            return statement;
        }

        String sql = sqlable.toSql();

        // Creates a new statement and caches it

        SqlBuilderLogger.out(
            statement == null ?
                "First creation of statement for SQL [ID = " + identifier + "]"
                    + " {" + sql + "}" :
                "Recreating statement since cached one is closed for SQL [ID = "
                    + identifier + "]" + " {" + sql + "}"
        );

        statement = returnGeneratedKeys ?
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) :
            connection.prepareStatement(sql);

        SqlBuilderCache.Statements.put(sql, statement);

        return statement;
    }

    /**
     * Binds the {@link PreparedStatement} with the given
     * list of bindable objects.
     * <p>
     * Actually this method calls {@link PreparedStatement#setObject(int, Object)}
     * for each bindable object.
     * @param statement the statement to bind
     * @param bindables the object which are bound
     * @return the bound statement
     * @throws SQLException if the bindable object list mismatch the prepared
     *                      statement parameters
     */
    public static PreparedStatement boundStatement(
        PreparedStatement statement, Iterable<Object> bindables) throws SQLException {

        if (bindables == null)
            return statement;

        int paramIndex = 1;

        String stmtHash = Integer.toHexString(statement.hashCode());

        for (Object bindable : bindables) {

            SqlBuilderLogger.out(
                "Binding " + paramIndex + "° value of type [" +
                    (bindable != null ?
                        bindable.getClass().getSimpleName() :
                        "Null"
                    ) + "] to value [" + bindable + "] | stmt = " + stmtHash);

            statement.setObject(paramIndex++, bindable);
        }

        return statement;
    }


    /**
     * Returns a bound statement for the given sqlbindable object retrieving the statement
     * from the cache if its exists or creating a new statement otherwise.
     * @param connection the connection
     * @param sqlbindable the sqlbindable object for which execute the update
     * @param identifier the cache's identifier of the sqlbindable
     * @param returnGeneratedKeys whether the RETURN_GENERATED_KEYS flag should
     *                            be passed for the creation of the statement
     * @return the bound statement
     * @throws SQLException if the bindable object list mismatch the prepared
     *                      statement parameters or if the statement creation fails
     */
    public static PreparedStatement getBoundStatement(
        Connection connection, SqlBindable sqlbindable,
        int identifier, boolean returnGeneratedKeys)
        throws SQLException {

        return boundStatement(
            getStatement(
                connection, sqlbindable, identifier, returnGeneratedKeys
            ),
            sqlbindable.getBindableObjects()
        );
    }

    // -------------------------------------------------------------------------
    // ------------------------- STATEMENTS EXECUTION --------------------------
    // -------------------------------------------------------------------------


    /**
     * Executes a query for a given sqlbindable object retrieving the statement
     * from the cache if its exists or creating a new statement otherwise.
     * @param connection the connection
     * @param sqlbindable the sqlbindable object for which execute the query
     * @param identifier the cache's identifier of the sqlbindable
     * @return the result set of the query
     * @throws SQLException if the query fails
     */
    public static ResultSet execQuery(
        Connection connection, SqlBindable sqlbindable, int identifier
    ) throws SQLException {

        return getBoundStatement(
            connection, sqlbindable, identifier, false
        ).executeQuery();
    }

    /**
     * Executes an update for a given sqlbindable object retrieving the statement
     * from the cache if its exists or creating a new statement otherwise.
     * @param connection the connection
     * @param sqlbindable the sqlbindable object for which execute the update
     * @param identifier the cache's identifier of the sqlbindable
     * @return the result of {@link PreparedStatement#executeUpdate()}
     *         which might either be the number of affected rows or the auto
     *         generated key if the flag was set for the statement.
     * @throws SQLException if the update fails
     */
    public static int execUpdate(
        Connection connection, SqlBindable sqlbindable,
        int identifier, boolean returnGeneratedKeys
    ) throws SQLException {

        return getBoundStatement(
            connection, sqlbindable, identifier, returnGeneratedKeys
        ).executeUpdate();
    }
}
