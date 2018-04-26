package com.docheinstein.sqlbuilder.expressions;

import com.docheinstein.sqlbuilder.SqlBindable;
import com.docheinstein.sqlbuilder.models.BindableObject;
import com.docheinstein.sqlbuilder.models.Column;
import com.docheinstein.sqlbuilder.statements.base.Statement;

import java.util.ArrayList;
import java.util.List;

public /* abstract */class Expression implements SqlBindable {
    private final String mOperatorKeyword;
    private final String mSqlPattern;
    private final SqlBindable mBindable1;
    private final SqlBindable mBindable2;

    /*
        Expression | Expression
        Expression | Column
        Expression | Object

        Column<T> | Column<T>
        Column<T> | Expression
        Column<T> | Object (T)
        Column | Statement      // Bonus for subqueries


        Object | Object
        Object | Expression
        Object (T) | Column<T>

        Statement
        Statement | Statement
    */

    //=========== Parametrized parentheses =============

    // Expression | Expression
    protected Expression(Expression e1, Expression e2, boolean parentheses,
                         boolean firstParentheses,  boolean secondParentheses) {
        this(e1, e2, parentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [1]: e | e");
    }

    // Expression | Column
    protected Expression(Expression e, Column c, boolean parentheses,
                         boolean firstParentheses,  boolean secondParentheses) {
        this(e, c, parentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [2]: e | c");
    }

    // Expression | Object
    protected Expression(Expression e, Object v, boolean parentheses,
                         boolean firstParentheses,  boolean secondParentheses) {
        this(e, new BindableObject(v), parentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [3]: e | v");
    }

    // Column<T> | Column<T>
    protected <T> Expression(Column<T> c1, Column<T> c2, boolean parentheses,
                             boolean firstParentheses,  boolean secondParentheses) {
        this(c1, c2, parentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [4]: c1 | c2");
    }

    // Column<T> | Expression
    protected <T> Expression(Column<T> c, Expression e, boolean parentheses,
                             boolean firstParentheses,  boolean secondParentheses) {
        this(c, e, parentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [5]: c | e");
    }

    // Column<T> | Object (T)
    protected <T> Expression(Column<T> c, T v, boolean parentheses,
                             boolean firstParentheses,  boolean secondParentheses) {
        this(c, new BindableObject(v), parentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [6]: c | v");
    }

    // Column<T> | Statement
    protected <T> Expression(Column<T> c, Statement s, boolean parentheses,
                             boolean firstParentheses,  boolean secondParentheses) {
        this(c, s, parentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [7]: c | s");
    }

    // Object (T) | Object (T)
    protected <T> Expression(T v1, T v2, boolean parentheses,
                         boolean firstParentheses,  boolean secondParentheses) {
        this(new BindableObject(v1), new BindableObject(v2),
            parentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [8]: v1 | v2");
    }

    // Statement
    protected Expression(Statement s, boolean parentheses,
                          boolean firstParentheses,  boolean secondParentheses) {
        this(null, s, parentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [9]: s");
    }

    // Statement | Statement
    protected Expression(Statement s1, Statement s2, boolean parentheses,
                          boolean firstParentheses,  boolean secondParentheses) {
        this(s1, s2, parentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [10]: s1 | s2");
    }

    //=========== Default parentheses =============

    // Expression | Column
    public Expression(Expression e, Column c) {
        this(e, c, true, false, false);
    }

    // Expression | Object
    public Expression(Expression e, Object v) {
        this(e, v, true, false, false);
    }

    // Expression | Expression
    public Expression(Expression e1, Expression e2) {
        this(e1, e2, true, false, false);
    }

    // Column<T> | Column<T>
    public <T> Expression(Column<T> c1, Column<T> c2) {
        this(c1, c2, true, false, false);
    }

    // Column<T> | Expression
    public <T> Expression(Column<T> c, Expression e) {
        this(c, e, true, false, false);
    }

    // Column<T> | Object (T)
    public <T> Expression(Column<T> c, T v) {
        this(c, v, true, false, false);
    }

    // Column<T> | Statement
    public <T> Expression(Column<T> c, Statement s) {
        this(c, s, true, false, false);
    }

    // Object (T) | Object (T)
    public <T> Expression(T v1, T v2) {
        this(v1, v2, true, false, false);
    }

    // Statement
    public Expression(Statement s) {
        this(s, true, false, false);
    }

    // Statement | Statement
    public Expression(Statement s1, Statement s2) {
        this(s1, s2, true, false, false);
    }


    protected Expression(SqlBindable bindable1,
                       SqlBindable bindable2,
                       boolean enclosingParentheses,
                       boolean bindable1Parentheses,
                       boolean bindable2Parentheses,
                       int dummy) {
//        System.out.println("---");
        mOperatorKeyword = getOperatorKeyword();

        String b1 = bindable1Parentheses ? "(%s)" : "%s";
        String b2 = bindable2Parentheses ? "(%s)" : "%s";
        String internalPattern;

        if (bindable1 != null && bindable2 != null) {
            internalPattern = b1 + " %s " + b2;
        }
        else if (bindable1 != null) {
            internalPattern = b1 + " %s";
        }
        else
            internalPattern = "%s "  + b2;

        mSqlPattern = enclosingParentheses ? "(" + internalPattern + ")" : internalPattern;
        mBindable1 = bindable1;
        mBindable2 = bindable2;
//        System.out.println("B1 + " + (bindable1 != null ? bindable1.getClass().getSimpleName() : "null"));
//        System.out.println("B2 + " + (bindable2 != null ? bindable2.getClass().getSimpleName() : "null"));
//        System.out.println("B1 + " + (bindable1 != null ? bindable1.toSql() : "null"));
//        System.out.println("B2 + " + (bindable2 != null ? bindable2.toSql() : "null"));
    }

    // ==================
    // === ARITHMETIC ===
    // ==================

    // +

    public Expression add(Expression e) { return new Operators.Add(this, e); }

    public Expression add(Object v) { return new Operators.Add(this, v); }

    public Expression add(Column c) { return new Operators.Add(this, c); }

    // -

    public Expression sub(Expression e) { return new Operators.Sub(this, e); }

    public Expression sub(Object v) { return new Operators.Sub(this, v); }

    public Expression sub(Column c) { return new Operators.Sub(this, c); }

    // *

    public Expression mul(Expression e) { return new Operators.Mul(this, e); }

    public Expression mul(Object v) { return new Operators.Mul(this, v); }

    public Expression mul(Column c) { return new Operators.Mul(this, c); }

    // /

    public Expression div(Expression e) { return new Operators.Div(this, e); }

    public Expression div(Object v) { return new Operators.Div(this, v); }

    public Expression div(Column c) { return new Operators.Div(this, c); }

    // %

    public Expression mod(Expression e) { return new Operators.Mod(this, e); }

    public Expression mod(Object v) { return new Operators.Mod(this, v); }

    public Expression mod(Column c) { return new Operators.Mod(this, c); }

    // ==================
    // === COMPARISON ===
    // ==================

    // =

    public Expression eq(Expression e) { return new Operators.Eq(this, e); }

    public Expression eq(Object v) { return new Operators.Eq(this, v); }

    public Expression eq(Column c) { return new Operators.Eq(this, c); }


    // =

    public Expression neq(Expression e) { return new Operators.Neq(this, e); }

    public Expression neq(Object v) { return new Operators.Neq(this, v); }

    public Expression neq(Column c) { return new Operators.Neq(this, c); }

    // >

    public Expression gt(Expression e) { return new Operators.Gt(this, e); }

    public Expression gt(Object v) { return new Operators.Gt(this, v); }

    public Expression gt(Column c) { return new Operators.Gt(this, c); }

    // >=

    public Expression ge(Expression e) { return new Operators.Ge(this, e); }

    public Expression ge(Object v) { return new Operators.Ge(this, v); }

    public Expression ge(Column c) { return new Operators.Ge(this, c); }

    // <

    public Expression lt(Expression e) { return new Operators.Lt(this, e); }

    public Expression lt(Object v) { return new Operators.Lt(this, v); }

    public Expression lt(Column c) { return new Operators.Lt(this, c); }

    // <=

    public Expression le(Expression e) { return new Operators.Le(this, e); }

    public Expression le(Object v) { return new Operators.Le(this, v); }

    public Expression le(Column c) { return new Operators.Le(this, c); }


    // ==================
    // ==== LOGICAL =====
    // ==================

    // AND

    public Expression and(Expression e) { return new Operators.And(this, e); }

    public Expression and(Object v) { return new Operators.And(this, v); }

    public Expression and(Column c) { return new Operators.And(this, c); }

    // OR

    public Expression or(Expression e) { return new Operators.Or(this, e); }

    public Expression or(Object v) { return new Operators.Or(this, v); }

    public Expression or(Column c) { return new Operators.Or(this, c); }

    // XOR

    public Expression xor(Expression e) { return new Operators.Xor(this, e); }

    public Expression xor(Object v) { return new Operators.Xor(this, v); }

    public Expression xor(Column c) { return new Operators.Xor(this, c); }

    // LIKE

    public Expression like(Expression e) { return new Operators.Like(this, e); }

    public Expression like(Object v) { return new Operators.Like(this, v); }

    public Expression like(Column c) { return new Operators.Like(this, c); }

    // BETWEEN

    public Expression between(Expression e1, Expression e2) { return new Operators.Between(this, e1, e2); }

    public Expression between(Expression e, Column c) { return new Operators.Between(this, e, c); }

    public Expression between(Expression e, Object v) { return new Operators.Between(this, e, v); }

    public <T> Expression between(Column<T> c1, Column<T> c2) { return new Operators.Between(this, c1, c2); }

    public <T> Expression between(Column<T> c, Expression e) { return new Operators.Between(this, c, e); }

    public <T> Expression between(Column<T> c, T v) { return new Operators.Between(this, c, v); }

    public <T> Expression between(T v1, T v2) { return new Operators.Between(this, v1, v2); }

    public <T> Expression between(Object v, Expression e) { return new Operators.Between(this, v, e); }

    // IS NULL

    public Expression isNull() { return new Operators.IsNull(this); }

    // IS NOT NULL

    public Expression isNotNull() { return new Operators.IsNotNull(this); }

    // ==================
    // ==== BITWISE =====
    // ==================

    public Expression bitAnd(Expression e) { return new Operators.BitAnd(this, e); }

    public Expression bitAnd(Object v) { return new Operators.BitAnd(this, v); }

    public Expression bitAnd(Column c) { return new Operators.BitAnd(this, c); }

    public Expression bitOr(Expression e) { return new Operators.BitOr(this, e); }

    public Expression bitOr(Object v) { return new Operators.BitOr(this, v); }

    public Expression bitOr(Column c) { return new Operators.BitOr(this, c); }

    public Expression bitXor(Expression e) { return new Operators.BitXor(this, e); }

    public Expression bitXor(Object v) { return new Operators.BitXor(this, v); }

    public Expression bitXor(Column c) { return new Operators.BitXor(this, c); }

    @Override
    public List<Object> getBindableObjects() {
        List<Object> objects = new ArrayList<>();

        if (mBindable1 != null) {
            List<Object> b1Objects = mBindable1.getBindableObjects();

            if (b1Objects != null)
                objects.addAll(b1Objects);
        }

        if (mBindable2 != null) {
            List<Object> b2Objects = mBindable2.getBindableObjects();

            if (b2Objects != null)
                objects.addAll(b2Objects);
        }

        return objects;
    }

    @Override
    public String toSql() {
        // <A> <OPERATOR> <B>
        if (mBindable1 != null && mBindable2 != null)
            return String.format(
                mSqlPattern,
                mBindable1.toSql(),
                mOperatorKeyword,
                mBindable2.toSql()
            );

        // <OPERATOR>
        if (mBindable1 == null && mBindable2 == null)
            return String.format(
                mSqlPattern,
                mOperatorKeyword
            );

        // <OPERATOR> <B>
        if (mBindable1 == null)
            return String.format(
                mSqlPattern,
                mOperatorKeyword,
                mBindable2.toSql()
            );

        // <A> <OPERATOR>
        // if (mBindable2 == null)
        return String.format(
            mSqlPattern,
            mBindable1.toSql(),
            mOperatorKeyword
        );
    }

    protected /* abstract */ String getOperatorKeyword() { return ""; }
}
