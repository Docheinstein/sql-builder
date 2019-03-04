package org.docheinstein.sqlbuilder.expressions;

import org.docheinstein.sqlbuilder.SqlBindable;
import org.docheinstein.sqlbuilder.commons.SqlBuilder;
import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.models.SqlBindableObject;
import org.docheinstein.sqlbuilder.models.Column;
import org.docheinstein.sqlbuilder.statements.base.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * Generically represents a SQL expression between two entities.
 *
 * <p>
 *
 * The basic structure of an expression is EXPRESSION_1 OPERATOR EXPRESSION_1 where
 * each expression can be obviously composed by other expressions.
 *
 * <p>
 *
 * This class contains all the possible methods for compose an arbitrary complex
 * expression; for this reason many methods exist for each operator.
 *
 * <p>
 *
 * An example of SQL expression can be a WHERE condition or a CHECK condition.
 */
public abstract class Expression implements SqlBindable {
    /**
     *  The keyword of the operator that links the two expression.
     *  <p>
     *  (e.g. AND, XOR, +, -, ...)
     **/
    private final String mOperatorKeyword;

    /**
     * The left bindable object which can provide a list of object
     * this expression have to be bound to.
     *
     * <p>
     *
     * Note that this can either be a simple {@link SqlBindableObject} or another
     * {@link Expression} (since an expression is a {@link SqlBindable} itself).
     */
    private final SqlBindable mBindable1;


    /**
     * The right bindable object which can provide a list of object
     * this expression have to be bound to.
     *
     * <p>
     *
     * Note that this can either be a simple {@link SqlBindableObject} or another
     * {@link Expression} (since an expression is a {@link SqlBindable} itself).
     */
    private final SqlBindable mBindable2;

    /** Whether the expression should be enclosed in outer parentheses. */
    private boolean mEnclosingParentheses;

    /**
     * Whether the first member of the expression
     * should be enclosed in parentheses.
     **/
    private boolean mFirstParentheses;

    /**
     * Whether the second member of the expression
     * should be enclosed in parentheses.
     **/
    private boolean mSecondParentheses;

    /*
        Expression | Expression
        Expression | Column
        Expression | Object

        Column<T> | Column<T>
        Column<T> | Expression
        Column<T> | Object (T)
        Column | Statement       // Subqueries


        Object | Object          // Quite dummy...
        Object | Expression
        Object (T) | Column<T>

        Statement
        Statement | Statement
    */


    /**
     * Returns the string to used as operator between the two nested expressions.
     * <p>
     * (e.g. AND, XOR, +, -)
     * @return the operator keyword
     */
    protected abstract String getOperatorKeyword();

    // -------------------------------------------------------------------------
    // ------------------------- Parametrized parentheses ----------------------
    // -------------------------------------------------------------------------

    /**
     * Creates an expression between an {@link Expression} and an {@link Expression}.
     * @param e1 the first expression
     * @param e2 the second expression
     * @param enclosingParentheses whether use outer parentheses for build the SQL string
     * @param firstParentheses whether the first expression should be wrapped into parentheses
     * @param secondParentheses whether the first expression should be wrapped into parentheses
     */
    // Expression | Expression
    protected Expression(Expression e1, Expression e2, boolean enclosingParentheses,
                         boolean firstParentheses,  boolean secondParentheses) {
        this(e1, e2, enclosingParentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [1]: e | e");
    }

    /**
     * Creates an expression between an {@link Expression} and an {@link Column}.
     * @param e the expression
     * @param c the column
     * @param enclosingParentheses whether use outer parentheses for build the SQL string
     * @param firstParentheses whether the first expression should be wrapped into parentheses
     * @param secondParentheses whether the first expression should be wrapped into parentheses
     */
    // Expression | Column
    protected Expression(Expression e, Column c, boolean enclosingParentheses,
                         boolean firstParentheses,  boolean secondParentheses) {
        this(e, c, enclosingParentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [2]: e | c");
    }

    /**
     * Creates an expression between an {@link Expression} and an {@link Object}.
     * @param e the expression
     * @param v the object (a primitive value, typically int, String, ...)
     * @param enclosingParentheses whether use outer parentheses for build the SQL string
     * @param firstParentheses whether the first expression should be wrapped into parentheses
     * @param secondParentheses whether the first expression should be wrapped into parentheses
     */
    // Expression | Object
    protected Expression(Expression e, Object v, boolean enclosingParentheses,
                         boolean firstParentheses,  boolean secondParentheses) {
        this(e, new SqlBindableObject(v), enclosingParentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [3]: e | v");
    }

    /**
     * Creates an expression between a {@link Column} and a {@link Column}.
     * @param c1 the first column
     * @param c2 the second column
     * @param enclosingParentheses whether use outer parentheses for build the SQL string
     * @param firstParentheses whether the first expression should be wrapped into parentheses
     * @param secondParentheses whether the first expression should be wrapped into parentheses
     * @param <T> the type of the columns (must be the same)
     */
    // Column<T> | Column<T>
    protected <T> Expression(Column<T> c1, Column<T> c2, boolean enclosingParentheses,
                             boolean firstParentheses,  boolean secondParentheses) {
        this(c1, c2, enclosingParentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [4]: c1 | c2");
    }

    /**
     * Creates an expression between a {@link Column} and an {@link Expression}.
     * @param c the column
     * @param e the expression
     * @param enclosingParentheses whether use outer parentheses for build the SQL string
     * @param firstParentheses whether the first expression should be wrapped into parentheses
     * @param secondParentheses whether the first expression should be wrapped into parentheses
     * @param <T> the type of the column
     */
    // Column<T> | Expression
    protected <T> Expression(Column<T> c, Expression e, boolean enclosingParentheses,
                             boolean firstParentheses,  boolean secondParentheses) {
        this(c, e, enclosingParentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [5]: c | e");
    }

    /**
     * Creates an expression between a {@link Column} and an {@link Object}.
     * @param c the column
     * @param v the object (a primitive value, typically int, String, ...)
     * @param enclosingParentheses whether use outer parentheses for build the SQL string
     * @param firstParentheses whether the first expression should be wrapped into parentheses
     * @param secondParentheses whether the first expression should be wrapped into parentheses
     * @param <T> the type of the column and of the object
     */
    // Column<T> | Object (T)
    protected <T> Expression(Column<T> c, T v, boolean enclosingParentheses,
                             boolean firstParentheses,  boolean secondParentheses) {
        this(c, new SqlBindableObject(v), enclosingParentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [6]: c | v");
    }

    /**
     * Creates an expression between a {@link Column} and an {@link Statement}.
     * @param c the column
     * @param s the statement
     * @param enclosingParentheses whether use outer parentheses for build the SQL string
     * @param firstParentheses whether the first expression should be wrapped into parentheses
     * @param secondParentheses whether the first expression should be wrapped into parentheses
     * @param <T> the type of the column
     */
    // Column<T> | Statement
    protected <T> Expression(Column<T> c, Statement s, boolean enclosingParentheses,
                             boolean firstParentheses,  boolean secondParentheses) {
        this(c, s, enclosingParentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [7]: c | s");
    }

    /**
     * Creates an expression between an {@link Object} and an {@link Object}.
     * @param v1 the first object (a primitive value, typically int, String, ...)
     * @param v2 the first object (a primitive value, typically int, String, ...)
     * @param enclosingParentheses whether use outer parentheses for build the SQL string
     * @param firstParentheses whether the first expression should be wrapped into parentheses
     * @param secondParentheses whether the first expression should be wrapped into parentheses
     * @param <T> the type of the objects (must be the same)
     */
    // Object (T) | Object (T)
    protected <T> Expression(T v1, T v2, boolean enclosingParentheses,
                         boolean firstParentheses,  boolean secondParentheses) {
        this(new SqlBindableObject(v1), new SqlBindableObject(v2),
            enclosingParentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [8]: v1 | v2");
    }

    /**
     * Creates an expression for a single {@link Statement}.
     * @param s the statement
     * @param enclosingParentheses whether use outer parentheses for build the SQL string
     * @param firstParentheses whether the first expression should be wrapped into parentheses
     * @param secondParentheses whether the first expression should be wrapped into parentheses
     */
    // Statement
    protected Expression(Statement s, boolean enclosingParentheses,
                          boolean firstParentheses,  boolean secondParentheses) {
        this(null, s, enclosingParentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [9]: s");
    }

    /**
     * Creates an expression between a {@link Statement} and a {@link Statement}.
     * @param s1 the first statement
     * @param s2 the second statement
     * @param enclosingParentheses whether use outer parentheses for build the SQL string
     * @param firstParentheses whether the first expression should be wrapped into parentheses
     * @param secondParentheses whether the first expression should be wrapped into parentheses
     */
    // Statement | Statement
    protected Expression(Statement s1, Statement s2, boolean enclosingParentheses,
                          boolean firstParentheses,  boolean secondParentheses) {
        this(s1, s2, enclosingParentheses, firstParentheses, secondParentheses, 0);
//        System.out.println("Constructor [10]: s1 | s2");
    }

    // -------------------------------------------------------------------------
    // --------------------------- Default parentheses -------------------------
    // -------------------------------------------------------------------------

    /**
     * Creates an expression between an {@link Expression} and a {@link Column}.
     * @param e the expression
     * @param c the column
     */
    // Expression | Column
    public Expression(Expression e, Column c) {
        this(e, c, true, false, false);
    }

    /**
     * Creates an expression between an {@link Expression} and an {@link Object}.
     * @param e the expression
     * @param v the object (a primitive value, typically int, String, ...)
     */
    // Expression | Object
    public Expression(Expression e, Object v) {
        this(e, v, true, false, false);
    }

    /**
     * Creates an expression between an {@link Expression} and an {@link Expression}.
     * @param e1 the first expression
     * @param e2 the second expression
     */
    // Expression | Expression
    public Expression(Expression e1, Expression e2) {
        this(e1, e2, true, false, false);
    }

    /**
     * Creates an expression between a {@link Column} and a {@link Column}.
     * @param c1 the first column
     * @param c2 the first column
     * @param <T> the type of the columns (must be the same)
     */
    // Column<T> | Column<T>
    public <T> Expression(Column<T> c1, Column<T> c2) {
        this(c1, c2, true, false, false);
    }

    /**
     * Creates an expression between a {@link Column} and an {@link Expression}.
     * @param c the column
     * @param e the expression
     * @param <T> the type of the column
     */
    // Column<T> | Expression
    public <T> Expression(Column<T> c, Expression e) {
        this(c, e, true, false, false);
    }

    /**
     * Creates an expression between a {@link Column} and an {@link Object}.
     * @param c the column
     * @param v the object (a primitive value, typically int, String, ...)
     * @param <T> the type of the column and of the object
     */
    // Column<T> | Object (T)
    public <T> Expression(Column<T> c, T v) {
        this(c, v, true, false, false);
    }

    /**
     * Creates an expression between a {@link Column} and an {@link Statement}.
     * @param c the column
     * @param s the statement
     * @param <T> the type of the column 
     */
    // Column<T> | Statement
    public <T> Expression(Column<T> c, Statement s) {
        this(c, s, true, false, false);
    }

    /**
     * Creates an expression between a {@link Object} and an {@link Object}.
     * @param v1 the first object (a primitive value, typically int, String, ...)
     * @param v2 the first object (a primitive value, typically int, String, ...)
     * @param <T> the type of the objects (must be the same)
     */
    // Object (T) | Object (T)
    public <T> Expression(T v1, T v2) {
        this(v1, v2, true, false, false);
    }

    /**
     * Creates an expression for a single {@link Statement}.
     * @param s the statement
     */
    // Statement
    public Expression(Statement s) {
        this(s, true, false, false);
    }

    /**
     * Creates an expression between a {@link Statement} and a {@link Statement}.
     * @param s1 the first statement
     * @param s2 the second statement
     */
    // Statement | Statement
    public Expression(Statement s1, Statement s2) {
        this(s1, s2, true, false, false);
    }

    /**
     * Creates an expression between two generic {@link SqlBindable}.
     * @param bindable1 the first bindable object
     * @param bindable2 the second bindable object
     * @param enclosingParentheses whether use outer parentheses for build the SQL string
     * @param firstParentheses whether the first expression should be wrapped into parentheses
     * @param secondParentheses whether the first expression should be wrapped into parentheses
     * @param dummy used for differentiate this method from the generic method
     *              because of type erasure
     */
    protected Expression(SqlBindable bindable1, 
                         SqlBindable bindable2, 
                         boolean enclosingParentheses, 
                         boolean firstParentheses,
                         boolean secondParentheses,
                         int dummy) {
        mBindable1 = bindable1;
        mBindable2 = bindable2;
        mOperatorKeyword = getOperatorKeyword();
        mEnclosingParentheses = enclosingParentheses;
        mFirstParentheses = firstParentheses;
        mSecondParentheses = secondParentheses;
    }

    // -------------------------------------------------------------------------
    // ---------------------------- ARITHMETIC ---------------------------------
    // -------------------------------------------------------------------------

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

    // -------------------------------------------------------------------------
    // ---------------------------- COMPARISON ---------------------------------
    // -------------------------------------------------------------------------

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


    // -------------------------------------------------------------------------
    // ----------------------------- LOGICAL -----------------------------------
    // -------------------------------------------------------------------------

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

    // -------------------------------------------------------------------------
    // ----------------------------- BITWISE -----------------------------------
    // -------------------------------------------------------------------------

    public Expression bitAnd(Expression e) { return new Operators.BitAnd(this, e); }

    public Expression bitAnd(Object v) { return new Operators.BitAnd(this, v); }

    public Expression bitAnd(Column c) { return new Operators.BitAnd(this, c); }

    public Expression bitOr(Expression e) { return new Operators.BitOr(this, e); }

    public Expression bitOr(Object v) { return new Operators.BitOr(this, v); }

    public Expression bitOr(Column c) { return new Operators.BitOr(this, c); }

    public Expression bitXor(Expression e) { return new Operators.BitXor(this, e); }

    public Expression bitXor(Object v) { return new Operators.BitXor(this, v); }

    public Expression bitXor(Column c) { return new Operators.BitXor(this, c); }


    // -------------------------------------------------------------------------
    // ----------------------------- UTILITIES ---------------------------------
    // -------------------------------------------------------------------------


    /**
     * Sets the parentheses for this expression.
     * @param enclosing whether this expression should be enclosed in parentheses
     * @param first  whether use parentheses for the first member
     * @param second whether use parentheses for the second member
     * @return this expression
     */
    public Expression parentheses(boolean enclosing, boolean first,
                                           boolean second) {
        return
            this
                .enclosingParentheses(enclosing)
                .firstParentheses(first)
                .secondParentheses(second);
    }


    /**
     * Sets whether this expression should be enclosed in outer parentheses.
     * @param yes whether this expression should be enclosed in parentheses
     * @return this expression
     */
    public Expression enclosingParentheses(boolean yes) {
        mEnclosingParentheses = yes;
        return this;
    }

    /**
     * Sets whether the first member of this expression should be enclosed
     * in parentheses;
     * @param yes whether use parentheses for the first member
     * @return this expression
     */
    public Expression firstParentheses(boolean yes) {
        mFirstParentheses = yes;
        return this;
    }

    /**
     * Sets whether the first member of this expression should be enclosed
     * in parentheses;
     * @param yes whether use parentheses for the second member
     * @return this expression
     */
    public Expression secondParentheses(boolean yes) {
        mSecondParentheses = yes;
        return this;
    }

    @Override
    public List<Object> getBindableObjects() {
        // Appends all the object from the first bindable to the objects
        // of the second bindable
//        SqlBuilderLogger.out("getBindableObjects on " + hashCode() + " [" + this.getClass().getSimpleName() + "]");
        List<Object> objects = new ArrayList<>();
//        SqlBuilderLogger.out(this.getClass().getSimpleName() + " Objects #1");
        if (mBindable1 != null) {
            List<Object> b1Objects = mBindable1.getBindableObjects();

            if (b1Objects != null) {
//                int i = 0;
//                for (Object b : b1Objects) {
//                    i++;
//                    SqlBuilderLogger.out(this.getClass().getSimpleName() + " " + i + "° object #1: " + b);
//                }
                objects.addAll(b1Objects);
            }

        }
//        SqlBuilderLogger.out(this.getClass().getSimpleName() + " Objects #2");

        if (mBindable2 != null) {
            List<Object> b2Objects = mBindable2.getBindableObjects();

            if (b2Objects != null) {
//                int i = 0;
//                for (Object b : b2Objects) {
//                    i++;
//                    SqlBuilderLogger.out(this.getClass().getSimpleName() + " " + i + "° object #2: " + b);
//                }
                objects.addAll(b2Objects);
            }
        }

//        SqlBuilderLogger.out(this.getClass().getSimpleName() + " END");


        return objects;
    }

    @Override
    public String toSql() {
        String sqlPattern = buildSqlPattern();

        // <A> <OPERATOR> <B>
        if (mBindable1 != null && mBindable2 != null)
            return String.format(
                sqlPattern,
                mBindable1.toSql(),
                mOperatorKeyword,
                mBindable2.toSql()
            );

        // <OPERATOR>
        if (mBindable1 == null && mBindable2 == null)
            return String.format(
                sqlPattern,
                mOperatorKeyword
            );

        // <OPERATOR> <B>
        if (mBindable1 == null)
            return String.format(
                sqlPattern,
                mOperatorKeyword,
                mBindable2.toSql()
            );

        // <A> <OPERATOR>
        // if (mBindable2 == null) always true
            return String.format(
                sqlPattern,
                mBindable1.toSql(),
                mOperatorKeyword
            );
    }

    private String buildSqlPattern() {
        String b1 = mFirstParentheses ? "(%s)" : "%s";
        String b2 = mSecondParentheses ? "(%s)" : "%s";
        String internalPattern;

        if (mBindable1 != null && mBindable2 != null) {
            internalPattern = b1 + " %s " + b2;
        }
        else if (mBindable1 != null) {
            internalPattern = b1 + " %s";
        }
        else
            internalPattern = "%s "  + b2;

        return
            mEnclosingParentheses ?
                "(" + internalPattern + ")" :
                internalPattern;
//        System.out.println("B1 + " + (bindable1 != null ? bindable1.getClass().getSimpleName() : "null"));
//        System.out.println("B2 + " + (bindable2 != null ? bindable2.getClass().getSimpleName() : "null"));
//        System.out.println("B1 + " + (bindable1 != null ? bindable1.toSql() : "null"));
//        System.out.println("B2 + " + (bindable2 != null ? bindable2.toSql() : "null"));
    }
}
