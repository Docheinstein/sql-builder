package com.docheinstein.sqlbuilder.expressions;


import com.docheinstein.sqlbuilder.models.Column;
import com.docheinstein.sqlbuilder.statements.Select;
import com.docheinstein.sqlbuilder.statements.base.Statement;

public class Operators {
    // ==================
    // === ARITHMETIC ===
    // ==================

    // +

    public static class Add extends Expression {
        public Add(Expression e1, Expression e2) { super(e1, e2); }
        public Add(Expression e, Column c) { super(e, c); }
        public Add(Expression e, Object v) { super(e, v); }
        public <T> Add(Column<T> c1, Column<T> c2) { super(c1, c2); }
        public <T> Add(Column<T> c, Expression e) { super(c, e); }
        public <T> Add(Column<T> c, T v) { super(c, v); }
        public <T> Add(T v1, T v2) { super(v1, v2); }

        @Override
        protected String getOperatorKeyword() {
            return "+";
        }
    }

    // -

    public static class Sub extends Expression {
        public Sub(Expression e1, Expression e2) { super(e1, e2); }
        public Sub(Expression e, Column c) { super(e, c); }
        public Sub(Expression e, Object v) { super(e, v); }
        public <T> Sub(Column<T> c1, Column<T> c2) { super(c1, c2); }
        public <T> Sub(Column<T> c, Expression e) { super(c, e); }
        public <T> Sub(Column<T> c, T v) { super(c, v); }
        public <T> Sub(T v1, T v2) { super(v1, v2); }

        @Override
        protected String getOperatorKeyword() {
            return "-";
        }
    }

    // *

    public static class Mul extends Expression {
        public Mul(Expression e1, Expression e2) { super(e1, e2); }
        public Mul(Expression e, Column c) { super(e, c); }
        public Mul(Expression e, Object v) { super(e, v); }
        public <T> Mul(Column<T> c1, Column<T> c2) { super(c1, c2); }
        public <T> Mul(Column<T> c, Expression e) { super(c, e); }
        public <T> Mul(Column<T> c, T v) { super(c, v); }
        public <T> Mul(T v1, T v2) { super(v1, v2); }

        @Override
        protected String getOperatorKeyword() {
            return "*";
        }
    }

    // /

    public static class Div extends Expression {
        public Div(Expression e1, Expression e2) { super(e1, e2); }
        public Div(Expression e, Column c) { super(e, c); }
        public Div(Expression e, Object v) { super(e, v); }
        public <T> Div(Column<T> c1, Column<T> c2) { super(c1, c2); }
        public <T> Div(Column<T> c, Expression e) { super(c, e); }
        public <T> Div(Column<T> c, T v) { super(c, v); }
        public <T> Div(T v1, T v2) { super(v1, v2); }

        @Override
        protected String getOperatorKeyword() {
            return "/";
        }
    }

    // %

    public static class Mod extends Expression {
        public Mod(Expression e1, Expression e2) { super(e1, e2); }
        public Mod(Expression e, Column c) { super(e, c); }
        public Mod(Expression e, Object v) { super(e, v); }
        public <T> Mod(Column<T> c1, Column<T> c2) { super(c1, c2); }
        public <T> Mod(Column<T> c, Expression e) { super(c, e); }
        public <T> Mod(Column<T> c, T v) { super(c, v); }
        public <T> Mod(T v1, T v2) { super(v1, v2); }

        @Override
        protected String getOperatorKeyword() {
            return "%";
        }
    }

    // ==================
    // === COMPARISON ===
    // ==================

    // =

    public static class Eq extends Expression {
        public Eq(Expression e1, Expression e2) { super(e1, e2); }
        public Eq(Expression e, Column c) { super(e, c); }
        public Eq(Expression e, Object v) { super(e, v); }
        public <T> Eq(Column<T> c1, Column<T> c2) { super(c1, c2); }
        public <T> Eq(Column<T> c, Expression e) { super(c, e); }
        public <T> Eq(Column<T> c, T v) { super(c, v); }
        public <T> Eq(T v1, T v2) { super(v1, v2); }

        @Override
        protected String getOperatorKeyword() {
            return "=";
        }
    }

    // <>

    public static class Neq extends Expression {
        public Neq(Expression e1, Expression e2) { super(e1, e2); }
        public Neq(Expression e, Column c) { super(e, c); }
        public Neq(Expression e, Object v) { super(e, v); }
        public <T> Neq(Column<T> c1, Column<T> c2) { super(c1, c2); }
        public <T> Neq(Column<T> c, Expression e) { super(c, e); }
        public <T> Neq(Column<T> c, T v) { super(c, v); }
        public <T> Neq(T v1, T v2) { super(v1, v2); }

        @Override
        protected String getOperatorKeyword() {
            return "<>";
        }
    }

    // >

    public static class Gt extends Expression {
        public Gt(Expression e1, Expression e2) { super(e1, e2); }
        public Gt(Expression e, Column c) { super(e, c); }
        public Gt(Expression e, Object v) { super(e, v); }
        public <T> Gt(Column<T> c1, Column<T> c2) { super(c1, c2); }
        public <T> Gt(Column<T> c, Expression e) { super(c, e); }
        public <T> Gt(Column<T> c, T v) { super(c, v); }
        public <T> Gt(T v1, T v2) { super(v1, v2); }

        @Override
        protected String getOperatorKeyword() {
            return ">";
        }
    }

    // >=

    public static class Ge extends Expression {
        public Ge(Expression e1, Expression e2) { super(e1, e2); }
        public Ge(Expression e, Column c) { super(e, c); }
        public Ge(Expression e, Object v) { super(e, v); }
        public <T> Ge(Column<T> c1, Column<T> c2) { super(c1, c2); }
        public <T> Ge(Column<T> c, Expression e) { super(c, e); }
        public <T> Ge(Column<T> c, T v) { super(c, v); }
        public <T> Ge(T v1, T v2) { super(v1, v2); }

        @Override
        protected String getOperatorKeyword() {
            return ">=";
        }
    }

    // <

    public static class Lt extends Expression {
        public Lt(Expression e1, Expression e2) { super(e1, e2); }
        public Lt(Expression e, Column c) { super(e, c); }
        public Lt(Expression e, Object v) { super(e, v); }
        public <T> Lt(Column<T> c1, Column<T> c2) { super(c1, c2); }
        public <T> Lt(Column<T> c, Expression e) { super(c, e); }
        public <T> Lt(Column<T> c, T v) { super(c, v); }
        public <T> Lt(T v1, T v2) { super(v1, v2); }

        @Override
        protected String getOperatorKeyword() {
            return "<";
        }
    }

    // <=

    public static class Le extends Expression {
        public Le(Expression e1, Expression e2) { super(e1, e2); }
        public Le(Expression e, Column c) { super(e, c); }
        public Le(Expression e, Object v) { super(e, v); }
        public <T> Le(Column<T> c1, Column<T> c2) { super(c1, c2); }
        public <T> Le(Column<T> c, Expression e) { super(c, e); }
        public <T> Le(Column<T> c, T v) { super(c, v); }
        public <T> Le(T v1, T v2) { super(v1, v2); }

        @Override
        protected String getOperatorKeyword() {
            return "<=";
        }
    }


    // ==================
    // ==== BITWISE =====
    // ==================

    // &

    public static class BitAnd extends Expression {
        public BitAnd(Expression e1, Expression e2) { super(e1, e2); }
        public BitAnd(Expression e, Column c) { super(e, c); }
        public BitAnd(Expression e, Object v) { super(e, v); }
        public <T> BitAnd(Column<T> c1, Column<T> c2) { super(c1, c2); }
        public <T> BitAnd(Column<T> c, Expression e) { super(c, e); }
        public <T> BitAnd(Column<T> c, T v) { super(c, v); }
        public <T> BitAnd(T v1, T v2) { super(v1, v2); }

        @Override
        protected String getOperatorKeyword() {
            return "&";
        }
    }

    // |

    public static class BitOr extends Expression {
        public BitOr(Expression e1, Expression e2) { super(e1, e2); }
        public BitOr(Expression e, Column c) { super(e, c); }
        public BitOr(Expression e, Object v) { super(e, v); }
        public <T> BitOr(Column<T> c1, Column<T> c2) { super(c1, c2); }
        public <T> BitOr(Column<T> c, Expression e) { super(c, e); }
        public <T> BitOr(Column<T> c, T v) { super(c, v); }
        public <T> BitOr(T v1, T v2) { super(v1, v2); }

        @Override
        protected String getOperatorKeyword() {
            return "|";
        }
    }

    // ^

    public static class BitXor extends Expression {
        public BitXor(Expression e1, Expression e2) { super(e1, e2); }
        public BitXor(Expression e, Column c) { super(e, c); }
        public BitXor(Expression e, Object v) { super(e, v); }
        public <T> BitXor(Column<T> c1, Column<T> c2) { super(c1, c2); }
        public <T> BitXor(Column<T> c, Expression e) { super(c, e); }
        public <T> BitXor(Column<T> c, T v) { super(c, v); }
        public <T> BitXor(T v1, T v2) { super(v1, v2); }

        @Override
        protected String getOperatorKeyword() {
            return "^";
        }
    }

    // ==================
    // ==== LOGICAL =====
    // ==================

    // AND

    public static class And extends Expression {
        public And(Expression e1, Expression e2) { super(e1, e2); }
        public And(Expression e, Column c) { super(e, c); }
        public And(Expression e, Object v) { super(e, v); }
        public <T> And(Column<T> c1, Column<T> c2) { super(c1, c2); }
        public <T> And(Column<T> c, Expression e) { super(c, e); }
        public <T> And(Column<T> c, T v) { super(c, v); }
        public <T> And(T v1, T v2) { super(v1, v2); }

        @Override
        protected String getOperatorKeyword() {
            return "AND";
        }
    }

    // OR

    public static class Or extends Expression {
        public Or(Expression e1, Expression e2) { super(e1, e2); }
        public Or(Expression e, Column c) { super(e, c); }
        public Or(Expression e, Object v) { super(e, v); }
        public <T> Or(Column<T> c1, Column<T> c2) { super(c1, c2); }
        public <T> Or(Column<T> c, Expression e) { super(c, e); }
        public <T> Or(Column<T> c, T v) { super(c, v); }
        public <T> Or(T v1, T v2) { super(v1, v2); }

        @Override
        protected String getOperatorKeyword() {
            return "OR";
        }
    }

    // XOR

    public static class Xor extends Expression {
        public Xor(Expression e1, Expression e2) { super(e1, e2); }
        public Xor(Expression e, Column c) { super(e, c); }
        public Xor(Expression e, Object v) { super(e, v); }
        public <T> Xor(Column<T> c1, Column<T> c2) { super(c1, c2); }
        public <T> Xor(Column<T> c, Expression e) { super(c, e); }
        public <T> Xor(Column<T> c, T v) { super(c, v); }
        public <T> Xor(T v1, T v2) { super(v1, v2); }

        @Override
        protected String getOperatorKeyword() {
            return "XOR";
        }
    }

    // LIKE

    public static class Like extends Expression {
        public Like(Expression e1, Expression e2) { super(e1, e2); }
        public Like(Expression e, Column c) { super(e, c); }
        public Like(Expression e, Object v) { super(e, v); }
        public <T> Like(Column<T> c1, Column<T> c2) { super(c1, c2); }
        public <T> Like(Column<T> c, Expression e) { super(c, e); }
        public <T> Like(Column<T> c, T v) { super(c, v); }
        public <T> Like(T v1, T v2) { super(v1, v2); }

        @Override
        protected String getOperatorKeyword() {
            return "LIKE";
        }
    }

    // BETWEEN

    public static class Between extends Expression {
        /*
            Expression | Expression, Expression
            Expression | Expression, Column
            Expression | Expression, Object

            Expression | Column, Column
            Expression | Column, Expression
            Expression | Column, Object

            Expression | Object, Object
            Expression | Object, Expression


            Column | Expression, Expression
            Column | Expression, Column
            Column | Expression, Object

            Column | Column, Column
            Column | Column, Expression
            Column | Column, Object

            Column | Object, Object
            Column | Object, Expression


            Object | Expression, Expression
            Object | Expression, Column
            Object | Expression, Object

            Object | Column, Column
            Object | Column, Expression
            Object | Column, Object

            Object | Object, Object
            Object | Object, Expression
        */

        public Between(Expression _e, Expression e1, Expression e2) { super(_e, e1.and(e2)); }
        public Between(Expression _e, Expression e, Column c) { super(_e, e.and(c)); }
        public Between(Expression _e, Expression e, Object v) { super(_e, e.and(v)); }

        public <T> Between(Expression _e, Column<T> c1, Column<T> c2) { super(_e, c1.and(c2)); }
        public     Between(Expression _e, Column c, Expression e) { super(_e, c.and(e)); }
        public <T> Between(Expression _e, Column<T> c, T v) { super(_e, c.and(v)); }

        public <T> Between(Expression _e, T v1, T v2) { super(_e, new And(v1, v2)); }
        public     Between(Expression _e, Object v, Expression e) { super(_e, new And(v, e)); }


        public <T> Between(Column<T> _c, Expression e1, Expression e2) { super(_c, e1.and(e2)); }
        public <T> Between(Column<T> _c, Expression e, Column c) { super(_c, e.and(c)); }
        public <T> Between(Column<T> _c, Expression e, Object v) { super(_c, e.and(v)); }

        public <T> Between(Column<T> _c, Column<T> c1, Column<T> c2) { super(_c, c1.and(c2)); }
        public <T> Between(Column<T> _c, Column c, Expression e) { super(_c, c.and(e)); }
        public <T> Between(Column<T> _c, Column<T> c, T v) { super(_c, c.and(v)); }

        public <T> Between(Column<T> _c, T v1, T v2) { super(_c, new And(v1, v2)); }
        public <T> Between(Column<T> _c, Object v, Expression e) { super(_c, new And(v, e)); }


        public     Between(Object _v, Expression e1, Expression e2) { super(_v, e1.and(e2)); }
        public     Between(Object _v, Expression e, Column c) { super(_v, e.and(c)); }
        public     Between(Object _v, Expression e, Object v) { super(_v, e.and(v)); }

        public <T> Between(T _v, Column<T> c1, Column<T> c2) { super(_v, c1.and(c2)); }
        public     Between(Object _v, Column c, Expression e) { super(_v, c.and(e)); }
        public <T> Between(T _v, Column<T> c, T v) { super(_v, c.and(v)); }

        public <T> Between(T _v, T v1, T v2) { super(_v, new And(v1, v2)); }
        public     Between(Object _v, Object v, Expression e) { super(_v, new And(v, e)); }

        @Override
        protected String getOperatorKeyword() {
            return "BETWEEN";
        }
    }

    // IS NULL

    public static class IsNull extends Expression {
        public IsNull(Expression e1) { super(e1, null, true, false, false, 0); }
        public <T> IsNull(Column<T> c) { super(c, null, true, false, false, 0); }
        public <T> IsNull(T v1) { super(v1, null); }

        @Override
        protected String getOperatorKeyword() {
            return "IS NULL";
        }
    }

    // IS NOT NULL

    public static class IsNotNull extends Expression {
        public IsNotNull(Expression e1) { super(e1, null, true, false, false, 0); }
        public <T> IsNotNull(Column<T> c) { super(c, null, true, false, false, 0); }
        public <T> IsNotNull(T v1) { super(v1, null); }

        @Override
        protected String getOperatorKeyword() {
            return "IS NOT NULL";
        }
    }


    // ============================
    // === SUB QUERY OPERATORS ====
    // ============================


    // IN

    public static class In extends Expression {
        public <T> In(Column<T> c, Select s) { super(c, s); }

        @Override
        protected String getOperatorKeyword() {
            return "IN";
        }
    }

    // NOT IN

    public static class NotIn extends Expression {
        public <T> NotIn(Column<T> c, Select s) { super(c, s, true, false, true); }

        @Override
        protected String getOperatorKeyword() {
            return "NOT IN";
        }
    }

    // SOME

    public static class Some extends Expression {
        public Some(Select s) { super(s, false, false, true); }

        @Override
        protected String getOperatorKeyword() {
            return "SOME";
        }
    }

    // ANY

    public static class Any extends Expression {
        public Any(Select s) { super(s, false, false, true); }

        @Override
        protected String getOperatorKeyword() {
            return "ANY";
        }
    }

    // ALL

    public static class All extends Expression {
        public All(Select s) { super(s, false, false, true); }

        @Override
        protected String getOperatorKeyword() {
            return "ALL";
        }
    }

    // EXISTS

    public static class Exists extends Expression {
        public Exists(Select s) { super(s, false, false, true); }

        @Override
        protected String getOperatorKeyword() {
            return "EXISTS";
        }
    }

    public static class NotExists extends Expression {
        public NotExists(Select s) { super(s, false, false, true); }

        @Override
        protected String getOperatorKeyword() {
            return "NOT EXISTS";
        }
    }

    // ==================
    // ===== BINARY =====
    // ==================

    public static class Except extends Expression {
        public Except(Select s1, Select s2) { super(s1, s2, false, true, true); }

        @Override
        protected String getOperatorKeyword() {
            return "EXCEPT";
        }
    }

    public static class Union extends Expression {
        public Union(Select s1, Select s2) { super(s1, s2, false, true, true); }

        @Override
        protected String getOperatorKeyword() {
            return "UNION";
        }
    }

    public static class Intersect extends Expression {
        public Intersect(Select s1, Select s2) { super(s1, s2, false, true, true); }

        @Override
        protected String getOperatorKeyword() {
            return "INTERSECT";
        }
    }
}
