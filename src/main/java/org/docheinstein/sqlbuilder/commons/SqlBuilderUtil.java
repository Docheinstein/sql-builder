package org.docheinstein.sqlbuilder.commons;

import org.docheinstein.sqlbuilder.SqlBindable;
import org.docheinstein.sqlbuilder.Sqlable;
import org.docheinstein.sqlbuilder.exceptions.UnsupportedSqlLanguage;
import org.docheinstein.sqlbuilder.expressions.Expression;
import org.docheinstein.sqlbuilder.models.Tuple;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Map;
import java.util.StringJoiner;

public class SqlBuilderUtil {
    public interface Listable<T> {
        String getString(T t);
    }

    /**
     * Scans an iterable collection of elements and for each of those
     * calls the {@link Listable#getString(Object)} method of
     * the given {@link Listable} interface.
     * The value is then appended to the list, separated with a comma.
     * @param elements the elements to scan
     * @param listable the interface that defines the rules
     *                 to use to current the string to append to the list
     * @param <T> any type
     * @return  a list of string created with the given rules separated by
     *          a comma. (e.g: 'Username, Password')
     */
    public static <T> String getAsCommaList(Iterable<T> elements,
                                            Listable<T> listable) {
        StringJoiner joiner = new StringJoiner(", ");
        elements.forEach(el -> joiner.add(listable.getString(el)));
        return joiner.toString();
    }

    public static String getAsStringList(Iterable<String> strings) {
        return getAsCommaList(strings, s -> s);
    }

    public static String getExpressionCondition(Expression expr, String conditionName) {
        if (expr == null)
            return "";

        StringBuilder sql = new StringBuilder();

        sql.append(" " + conditionName + " (");
        sql.append(expr.toSql());
        sql.append(")");

        return sql.toString();
    }

    public static <T extends Tuple> Map<String, Field> getCachedColumnFieldOrCreateAndCache(Class<T> clazz)
        throws IllegalAccessException, InstantiationException {

        Map<String, Field> columnFieldMap = SqlBuilderCache.Tuples.get(clazz);

        if (columnFieldMap == null) {
            columnFieldMap = clazz.newInstance().getColumnFieldMap();
            SqlBuilderCache.Tuples.put(clazz, columnFieldMap);
        }
        return columnFieldMap;
    }

    public static PreparedStatement getCachedStatementOrCreate(
        Connection connection, String sql) throws SQLException {
        return getCachedStatementOrCreate(connection, sql, false);
    }
    public static PreparedStatement getCachedStatementOrCreate(
        Connection connection, String sql, boolean returnGeneratedKeys) throws SQLException {

        PreparedStatement statement = SqlBuilderCache.Statements.get(sql);

        if (statement == null) {
            SqlBuilderLogger.out("First creation of statement from sql: " + sql);
            if (returnGeneratedKeys)
                statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            else
                statement = connection.prepareStatement(sql);
            SqlBuilderCache.Statements.put(sql, statement);
        } else {
            SqlBuilderLogger.out("Reusing cached statement for sql: " + sql);
        }

        return statement;
    }

    public static PreparedStatement getCachedStatementOrCreateOrRegenerate(
        Connection connection, Sqlable sqlable, int identifier, boolean returnGeneratedKeys) throws SQLException {

        PreparedStatement statement = SqlBuilderCache.Statements.get(identifier);

        if (statement == null || statement.isClosed()) {
            String sql = sqlable.toSql();
            if (statement == null)
                SqlBuilderLogger.out("First creation of statement from sql: " + sql + " | ID = " + identifier);
            else
                SqlBuilderLogger.out("Recreating statement since cached one is closed for sql: "
                    + sql + " | ID = " + identifier);
            if (returnGeneratedKeys)
                statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            else
                statement = connection.prepareStatement(sql);
            SqlBuilderCache.Statements.put(identifier, statement);
        } else {
            SqlBuilderLogger.out("Reusing cached statement for sql with ID = " + identifier);
        }

        return statement;
    }

    public static PreparedStatement getBoundStatement(
        PreparedStatement statement, Iterable<Object> bindables) throws SQLException {

        if (bindables == null)
            return statement;

        int paramIndex = 1;

        for (Object bindable : bindables) {
            String bindableType = bindable != null ?
                bindable.getClass().getSimpleName() : "Null";
            SqlBuilderLogger.out("Binding " + paramIndex + "° value of type [" +
                        bindableType + "] to value [" + bindable + "]");
            statement.setObject(paramIndex++, bindable);
        }

        return statement;
    }

    public static ResultSet execQueryCachedStatementOrCreateOrRegenerate(
        Connection connection, SqlBindable sqlbindable, int identifier
    ) throws SQLException {

        return getBoundCachedStatementOrCreateOrRegenerate(
            connection, sqlbindable, identifier, false).executeQuery();
    }

    public static int execUpdateCachedStatementOrCreateOrRegenerate(
        Connection connection, SqlBindable sqlbindable,
        int identifier, boolean returnGeneratedKeys
    ) throws SQLException {

        return getBoundCachedStatementOrCreateOrRegenerate(
            connection, sqlbindable, identifier, returnGeneratedKeys).executeUpdate();
    }

    public static PreparedStatement getBoundCachedStatementOrCreateOrRegenerate(
        Connection connection, SqlBindable sqlbindable,
        int identifier, boolean returnGeneratedKeys)
    throws SQLException {

        return SqlBuilderUtil.getBoundStatement(
            SqlBuilderUtil.getCachedStatementOrCreateOrRegenerate(
                connection, sqlbindable, identifier, returnGeneratedKeys
            ),

            sqlbindable.getBindableObjects()
        );
    }

    public static void ensureLanguage(SqlLanguage sqlLanguage) {
        if (sqlLanguage != SqlBuilder.getLanguage())
            throw new UnsupportedSqlLanguage();
    }
}
