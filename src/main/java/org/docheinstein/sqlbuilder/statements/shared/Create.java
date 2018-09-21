package org.docheinstein.sqlbuilder.statements.shared;

import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.expressions.Expression;
import org.docheinstein.sqlbuilder.clauses.ForeignKey;
import org.docheinstein.sqlbuilder.models.Column;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.base.UpdateStatement;

import java.util.List;

/*
 * CREATE TABLE IF NOT EXISTS User (
 *   Username varchar(32) PRIMARY KEY,
 *   Password varchar(32),
 *   Capabilities int
 * );
 */

/**
 * Represents a CREATE TABLE statement.
 *
 * <p>
 *
 * This statement implementation differs from other statement's implementation since this
 * statement essentially requires a {@link Table} object instead of provide
 * methods for inline building of the statement.
 */
public class Create implements UpdateStatement {

    /** Table to create. */
    private Table mTable;

    /** Whether use the IF NOT EXISTS clause. */
    private boolean mIfNotExists;

    /**
     * Creates a CREATE TABLE statement for the given table.
     * @param table the table to create
     */
    public Create(Table table) {
        mTable = table;
    }

    /**
     * Sets/unsets the IF NOT EXISTS clause.
     * @param ifNotExists whether set the IF NOT EXISTS clause
     * @return this statement
     */
    public Create ifNotExists(boolean ifNotExists) {
        mIfNotExists = ifNotExists;
        return this;
    }

    /**
     * Sets the IF NOT EXISTS clause.
     * @return this statement
     */
    public Create ifNotExists() {
        return ifNotExists(true);
    }

    @Override
    public String toSql() {
        StringBuilder sql = new StringBuilder("CREATE TABLE ");

        if (mIfNotExists)
            sql.append("IF NOT EXISTS ");

        sql.append(mTable.getName());
        sql.append(" (");

        // Columns
        sql.append(SqlBuilderInternalUtil.getAsCommaList(
            mTable.getColumns(), Column::getColumnDefinition)
        );

        List<String> primaryKeyCols = mTable.getPrimaryKey();

        if (primaryKeyCols.size() > 0) {
            sql.append(", PRIMARY KEY (");
            sql.append(SqlBuilderInternalUtil.getAsStringList(primaryKeyCols));
            sql.append(")");
        }

        // Foreign keys
        List<ForeignKey> foreignKeys = mTable.getForeignKeys();
        for (ForeignKey fk : foreignKeys) {
            sql.append(", FOREIGN KEY (");
            sql.append(fk.getInternalColumn().getName());
            sql.append(") REFERENCES ");
            sql.append(fk.getExternalColumn().getTable());
            sql.append("(");
            sql.append(fk.getExternalColumn().getName());
            sql.append(")");

            ForeignKey.ReferenceAction deleteOption = fk.getOnDeleteOption();
            if (deleteOption != null) {
                sql.append(" ON DELETE ");
                sql.append(deleteOption.toSql());
            }

            ForeignKey.ReferenceAction updateOption = fk.getOnUpdateOption();
            if (updateOption != null) {
                sql.append(" ON UPDATE ");
                sql.append(updateOption.toSql());
            }
        }

        // Constraints

        // -- Check
        Expression checkExpression = mTable.getCheck();
        if (checkExpression != null) {
            sql.append(", CHECK ");
            sql.append(checkExpression.toSql());
        }

        sql.append("); ");

        String sqlStr = sql.toString();

        SqlBuilderLogger.out("Created [CREATE TABLE] SQL {" + sqlStr + "}");

        return sqlStr;
    }

    @Override
    public List<Object> getBindableObjects() {
        return mTable.getCheck() == null ? null : mTable.getCheck().getBindableObjects();
    }
}
