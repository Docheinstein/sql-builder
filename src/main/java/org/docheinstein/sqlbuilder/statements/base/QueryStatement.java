package org.docheinstein.sqlbuilder.statements.base;

import org.docheinstein.sqlbuilder.common.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.common.SqlBuilderUtil;
import org.docheinstein.sqlbuilder.models.Column;
import org.docheinstein.sqlbuilder.models.Tuple;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class QueryStatement implements Statement {
    public ResultSet exec(Connection connection) throws SQLException {
        if (connection == null)
            return null;

        return SqlBuilderUtil.getBoundStatement(
            SqlBuilderUtil.getCachedStatementOrCreate(
                connection, toSql()), getBindableObjects()
        ).executeQuery();
    }

    public ResultSet execCache(Connection connection, int identifier) throws SQLException {
        if (connection == null)
            return null;

        return SqlBuilderUtil.execQueryCachedStatementOrCreateOrRegenerate(
            connection, this, identifier);
    }

    public <T extends Tuple> List<T> fetch(Connection connection, Class<T> clazz)
        throws SQLException {
        List<T> list = new ArrayList<>();
        forEach(connection, clazz, list::add);
        return list;
    }

    public <T extends Tuple> List<T> fetchCache(Connection connection, Class<T> clazz,
                                                int identifier)
        throws SQLException {
        List<T> list = new ArrayList<>();
        forEachCache(connection, clazz, list::add, identifier);
        return list;
    }

    public interface ResultSetConsumer<T> {
        void onRowFetched(T t);
    }

    public <T extends Tuple> void forEach(Connection connection, Class<T> clazz,
                                          ResultSetConsumer<T> consumer)
        throws SQLException {
        forEachInternal(clazz, consumer, exec(connection));
    }

    public <T extends Tuple> void forEachCache(Connection connection, Class<T> clazz,
                                                ResultSetConsumer<T> consumer, int identifier)
        throws SQLException {
        forEachInternal(clazz, consumer, execCache(connection, identifier));
    }


    private <T extends Tuple> void forEachInternal(
        Class<T> clazz, ResultSetConsumer<T> consumer,
        ResultSet resultSet) throws SQLException {

        try {
            Map<String, Field> columnFieldMap = SqlBuilderUtil.getCachedColumnFieldOrCreateAndCache(clazz);

            while (resultSet.next()) {
                T t = clazz.newInstance();
                for (Column column : getFilteredColumns()) {
                    if (!column.isWellDefined())
                        throw new RuntimeException("Can't fetch a string column, provided a valid one");
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
                consumer.onRowFetched(t);
            }
        }
        catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("Fetch failed for type: " + clazz.getName());
        }
    }

    protected abstract List<Column> getFilteredColumns();

}
