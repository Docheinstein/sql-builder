package org.docheinstein.sqlbuilder.expressions;


import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;
import org.docheinstein.sqlbuilder.commons.SqlLanguage;
import org.docheinstein.sqlbuilder.models.Column;
import org.docheinstein.sqlbuilder.statements.base.QueryStatement;
import org.docheinstein.sqlbuilder.statements.shared.Select;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * Contains methods for create (almost?) all the SQL expressions ({@link Expression}).
 */
public class Operators {

    // -------------------------------------------------------------------------
    // ---------------------------- ARITHMETIC ---------------------------------
    // -------------------------------------------------------------------------

    // +

    public static Expression add(Object o1, Object o2) {
        return new Add(o1, o2);
    }

    public static class Add extends Expression {
        public Add(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return "+";
        }
    }

    // -

    public static Expression sub(Object o1, Object o2) {
        return new Sub(o1, o2);
    }

    public static class Sub extends Expression {
        public Sub(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return "-";
        }
    }

    // *

    public static Expression mul(Object o1, Object o2) {
        return new Mul(o1, o2);
    }

    public static class Mul extends Expression {
        public Mul(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return "*";
        }
    }

    // /

    public static Expression div(Object o1, Object o2) {
        return new Div(o1, o2);
    }

    public static class Div extends Expression {
        public Div(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return "/";
        }
    }

    // %

    public static Expression mod(Object o1, Object o2) {
        return new Mod(o1, o2);
    }

    public static class Mod extends Expression {
        public Mod(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return "%";
        }
    }

    // -------------------------------------------------------------------------
    // ---------------------------- COMPARISON ---------------------------------
    // -------------------------------------------------------------------------

    // =

    public static Expression eq(Object o1, Object o2) {
        return new Eq(o1, o2);
    }

    public static class Eq extends Expression {
        public Eq(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return "=";
        }
    }

    // <>

    public static Expression neq(Object o1, Object o2) {
        return new Neq(o1, o2);
    }

    public static class Neq extends Expression {
        public Neq(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return "<>";
        }
    }

    // >

    public static Expression gt(Object o1, Object o2) {
        return new Gt(o1, o2);
    }

    public static class Gt extends Expression {
        public Gt(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return ">";
        }
    }

    // >=

    public static Expression ge(Object o1, Object o2) {
        return new Ge(o1, o2);
    }

    public static class Ge extends Expression {
        public Ge(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return ">=";
        }
    }

    // <

    public static Expression lt(Object o1, Object o2) {
        return new Lt(o1, o2);
    }

    public static class Lt extends Expression {
        public Lt(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return "<";
        }
    }

    // <=

    public static Expression le(Object o1, Object o2) {
        return new Le(o1, o2);
    }

    public static class Le extends Expression {
        public Le(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return "<=";
        }
    }

    // -------------------------------------------------------------------------
    // ----------------------------- LOGICAL -----------------------------------
    // -------------------------------------------------------------------------

    // AND

    public static Expression and(Object o1, Object o2) {
        return new And(o1, o2);
    }

    public static class And extends Expression {
        public And(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return "AND";
        }
    }

    // OR

    public static Expression or(Object o1, Object o2) {
        return new Or(o1, o2);
    }

    public static class Or extends Expression {
        public Or(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return "OR";
        }
    }

    // XOR

    public static Expression xor(Object o1, Object o2) {
        return new Xor(o1, o2);
    }

    public static class Xor extends Expression {
        public Xor(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return "XOR";
        }
    }

    // LIKE

    public static Expression like(Object o1, Object o2) {
        return new Like(o1, o2);
    }

    public static class Like extends Expression {
        public Like(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return "LIKE";
        }
    }

    // BETWEEN

    public static Expression between(Object o1, Object o2, Object o3) {
        return new Between(o1, o2, o3);
    }

    public static class Between extends Expression {
        /*
         * Sanitize the AND expression in BETWEEN ? AND ? since MySQL doesn't
         * accept enclosing parentheses (i.e. BETWEEN ( ? AND ? ) is not accepted).
         * The correct one is (BETWEEN ? AND ?).
         */
        public Between(Object o1, Object o2, Object o3) {
            super(o1, Operators.and(o2, o3).parentheses(false, true, true));
        }

        @Override
        protected String getOperatorKeyword() {
            return "BETWEEN";
        }
    }


    // IS NULL

    public static Expression isNull(Object o1) {
        return new IsNull(o1);
    }

    public static class IsNull extends Expression {
        public IsNull(Object o1) { super(o1); }

        @Override
        protected String getOperatorKeyword() {
            return "IS NULL";
        }
    }

    // IS NOT NULL

    public static Expression isNotNull(Object o1) {
        return new IsNotNull(o1);
    }

    public static class IsNotNull extends Expression {
        public IsNotNull(Object o1) { super(o1); }

        @Override
        protected String getOperatorKeyword() {
            return "IS NOT NULL";
        }
    }

    // -------------------------------------------------------------------------
    // ----------------------------- BITWISE -----------------------------------
    // -------------------------------------------------------------------------

    // &

    public static Expression bitAnd(Object o1, Object o2) {
        return new BitAnd(o1, o2);
    }

    public static class BitAnd extends Expression {
        public BitAnd(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return "&";
        }
    }

    // |

    public static Expression bitOr(Object o1, Object o2) {
        return new BitOr(o1, o2);
    }

    public static class BitOr extends Expression {
        public BitOr(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return "&";
        }
    }

    // ^

    public static Expression bitXor(Object o1, Object o2) {
        return new BitXor(o1, o2);
    }

    public static class BitXor extends Expression {
        public BitXor(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return "^";
        }
    }

    // -------------------------------------------------------------------------
    // --------------------------- SUB QUERY -----------------------------------
    // -------------------------------------------------------------------------

    // IN

    public static Expression in(Object o1, Object o2) {
        return new In(o1, o2);
    }

    public static class In extends Expression {
        public In(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return "IN";
        }
    }

    // NOT IN

    public static Expression notIn(Object o1, Object o2) {
        return new NotIn(o1, o2);
    }

    public static class NotIn extends Expression {
        public NotIn(Object o1, Object o2) { super(o1, o2); }

        @Override
        protected String getOperatorKeyword() {
            return "NOT IN";
        }
    }

    // SOME

    public static Expression some(Object o1) {
        return new Some(o1);
    }

    public static class Some extends Expression {
        public Some(Object o1) { super(o1, null, false, false, true); }

        @Override
        protected String getOperatorKeyword() {
            return "SOME";
        }
    }

    // ANY

    public static Expression any(Object o1) {
        return new Any(o1);
    }

    public static class Any extends Expression {
        public Any(Object o1) { super(o1, null, false, false, true); }

        @Override
        protected String getOperatorKeyword() {
            return "ANY";
        }
    }

    // ALL

    public static Expression all(Object o1) {
        return new All(o1);
    }

    public static class All extends Expression {
        public All(Object o1) { super(o1, null, false, false, true); }

        @Override
        protected String getOperatorKeyword() {
            return "ALL";
        }
    }

    // EXISTS

    public static Expression exists(Object o1) {
        return new Exists(o1);
    }

    public static class Exists extends Expression {
        public Exists(Object o1) { super(o1, null, false, false, true); }

        @Override
        protected String getOperatorKeyword() {
            return "EXISTS";
        }
    }

    public static Expression notExists(Object o1) {
        return new NotExists(o1);
    }

    public static class NotExists extends Expression {
        public NotExists(Object o1) { super(o1, null, false, false, true); }

        @Override
        protected String getOperatorKeyword() {
            return "NOT EXISTS";
        }
    }

    // -------------------------------------------------------------------------
    // ----------------------------- BINARY ------------------------------------
    // -------------------------------------------------------------------------


    public static abstract class BinaryQueryStatement
        extends Expression implements QueryStatement {
        private final List<Column> mColumns;

        public BinaryQueryStatement(Select s1, Select s2) {
            super(s1, s2, false, true, true);

            if (s1.getColumns().size() != s2.getColumns().size())
                throw new InvalidParameterException(
                    "The binary query's semantic requires the SELECT statements to have the same column size");

            // The columns from the first select must be chosen so that for
            // the outer binary query the provided columns will actually be
            // the ones on which the query statement should work on
            mColumns = s1.getColumns();
        }

        @Override
        public List<Column> getColumns() {
            return mColumns;
        }
    }

    public static class Union extends BinaryQueryStatement {
        public Union(Select s1, Select s2) { super(s1, s2); }

        @Override
        protected String getOperatorKeyword() {
            return "UNION";
        }
    }

    public static class Except extends BinaryQueryStatement {
        public Except(Select s1, Select s2) {
            super(s1, s2);
            // MySQL doesn't support EXCEPT
            SqlBuilderInternalUtil.throwIfCurrentLanguageIs(SqlLanguage.MySQL);
        }

        @Override
        protected String getOperatorKeyword() {
            return "EXCEPT";
        }
    }

    public static class Intersect extends BinaryQueryStatement {
        public Intersect(Select s1, Select s2) {
            super(s1, s2);
            // MySQL doesn't support INTERSECT
            SqlBuilderInternalUtil.throwIfCurrentLanguageIs(SqlLanguage.MySQL);
        }

        @Override
        protected String getOperatorKeyword() {
            return "INTERSECT";
        }
    }
}
