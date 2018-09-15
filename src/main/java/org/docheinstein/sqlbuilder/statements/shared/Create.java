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
CREATE TABLE IF NOT EXISTS User (
    Username varchar(64) PRIMARY KEY,
    Password varchar(128),
    Capabilities int
);
*/

public class Create extends UpdateStatement {
    private Table mTable;
    private boolean mIfNotExists;

    public Create(Table table) {
        mTable = table;
    }

    public Create ifNotExists(boolean ifNotExists) {
        mIfNotExists = ifNotExists;
        return this;
    }

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

            ForeignKey.ReferenceOption deleteOption = fk.getOnDeleteOption();
            if (deleteOption != null) {
                sql.append(" ON DELETE ");
                sql.append(deleteOption.toSql());
            }

            ForeignKey.ReferenceOption updateOption = fk.getOnUpdateOption();
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

        SqlBuilderLogger.out("Created SQL: " + sqlStr);

        return sqlStr;
    }

    @Override
    public List<Object> getBindableObjects() {
        return null;
    }
}
