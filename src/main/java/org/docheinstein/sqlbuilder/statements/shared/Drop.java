package org.docheinstein.sqlbuilder.statements.shared;

import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.base.UpdateStatement;

import java.util.List;

/*
 * DROP TABLE User;
 */

/**
 * Represents a DROP TABLE statement.
 */
public class Drop implements UpdateStatement {

    /** Table to drop. */
    private Table mTable;

    /**
     * Creates a DROP TABLE statement for the given table.
     * @param table the table
     */
    public Drop(Table table) {
        mTable = table;
    }

    @Override
    public String toSql() {
        StringBuilder sql = new StringBuilder("DROP TABLE ");
        sql.append(mTable.getName());

        String sqlStr = sql.toString();

        SqlBuilderLogger.out("Created [DROP TABLE] SQL {" + sqlStr + "}");

        return sqlStr;
    }

    @Override
    public List<Object> getBindableObjects() {
        return null; // No parameters to bind
    }
}
