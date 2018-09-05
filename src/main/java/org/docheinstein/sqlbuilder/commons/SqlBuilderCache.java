package org.docheinstein.sqlbuilder.commons;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

public class SqlBuilderCache {

    public static class Tuples {
        private static final Map<Class, Map<String, Field>>
            TUPLE_2_COLUMN_FIELD_MAP = new HashMap<>();

        public static void put(Class clazz, Map<String, Field> columnField) {
            TUPLE_2_COLUMN_FIELD_MAP.put(clazz, columnField);
        }

        public static Map<String, Field> get(Class clazz) {
            return TUPLE_2_COLUMN_FIELD_MAP.get(clazz);
        }
    }

    public static class Statements {
        private static final Map<Integer, PreparedStatement>
            IDENTIFIER_2_STATEMENT_MAP = new HashMap<>();

        private static final Map<String, PreparedStatement>
            SQL_2_STATEMENT_MAP = new HashMap<>();

        public static PreparedStatement get(int identifier) {
            return IDENTIFIER_2_STATEMENT_MAP.get(identifier);
        }

        public static void put(int identifier, PreparedStatement statement) {
            IDENTIFIER_2_STATEMENT_MAP.put(identifier, statement);
        }

        public static PreparedStatement get(String sql) {
            return SQL_2_STATEMENT_MAP.get(sql);
        }

        public static void put(String sql, PreparedStatement statement) {
            SQL_2_STATEMENT_MAP.put(sql, statement);
        }
    }

}
