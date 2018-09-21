package org.docheinstein.sqlbuilder.commons;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

/**
 * Internal cache used by the library.
 */
class SqlBuilderCache {

    /**
     * Wraps a map that associates a class to its fields marked with
     * {@link org.docheinstein.sqlbuilder.models.ColumnField}.
     */
    static class Tuples {

        /**
         * The wrapped map that associates the classes with their fields
         * marked with {@link org.docheinstein.sqlbuilder.models.ColumnField}.
         */
        private static final Map<Class, Map<String, Field>>
            TUPLE_TO_COLUMN_FIELDS_MAP = new HashMap<>();

        /**
         * Inserts an association between a class and its column fields.
         * @param clazz the class
         * @param columnField the map that contains the class' column fields.
         */
        static void put(Class clazz, Map<String, Field> columnField) {
            TUPLE_TO_COLUMN_FIELDS_MAP.put(clazz, columnField);
        }

        /**
         * Returns a map of the column fields of the given class.
         * @param clazz the class
         * @return the column fields of the class
         */
        static Map<String, Field> get(Class clazz) {
            return TUPLE_TO_COLUMN_FIELDS_MAP.get(clazz);
        }
    }

    /**
     * Wraps a map that associates a statement to its identifier or sql string.
     */
    static class Statements {
        /**
         * The wrapped map that associates the cached statement with an identifier.
         * <p>
         * This map is typically faster to use compared to the SQL_TO_STATEMENT_MAP
         * because the only thing to now for use this map is the identifier of
         * the statement and there is no need to create the SQL string of the
         * statement each time.
         */
        private static final Map<Integer, PreparedStatement>
            IDENTIFIER_TO_STATEMENT_MAP = new HashMap<>();

        /**
         * The wrapped map that associates the cached statement with its sql
         * string.
         * <p>
         * This map is typically slower to use compared to IDENTIFIER_TO_STATEMENT_MAP
         * because for retrieve the statement the SQL string of the statement
         * must be created each time, however is easier to use because there
         * is no need to keep track of the identifier of the statement.
         */
        private static final Map<String, PreparedStatement>
            SQL_TO_STATEMENT_MAP = new HashMap<>();


        /**
         * Inserts an association between an identifier and a {@link PreparedStatement}.
         * @param identifier an identifier
         * @param statement the statement
         */
        static void put(int identifier, PreparedStatement statement) {
            IDENTIFIER_TO_STATEMENT_MAP.put(identifier, statement);
        }

        /**
         * Inserts an association between a sql string and a {@link PreparedStatement}.
         * @param sql a sql string
         * @param statement the statement
         */
        static void put(String sql, PreparedStatement statement) {
            SQL_TO_STATEMENT_MAP.put(sql, statement);
        }

        /**
         * Returns the cached {@link PreparedStatement} associated with
         * an identifier.
         * @param identifier the identifier of the statement
         * @return the {@link PreparedStatement} associated with the identifier
         *         or null if it doesn't exist
         */
        static PreparedStatement get(int identifier) {
            return IDENTIFIER_TO_STATEMENT_MAP.get(identifier);
        }


        /**
         * Returns the cached {@link PreparedStatement} associated with
         * a sql string.
         * @param sql the sql string of the statement
         * @return the {@link PreparedStatement} associated with the sql string
         *         or null if it doesn't exist
         */
        static PreparedStatement get(String sql) {
            return SQL_TO_STATEMENT_MAP.get(sql);
        }

    }

}
