package org.docheinstein.sqlbuilder.expressions;

import org.docheinstein.sqlbuilder.SqlBindable;
import org.docheinstein.sqlbuilder.commons.SqlBindableFactory;
import org.docheinstein.sqlbuilder.models.SqlBindableObject;

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

    /**
     * Returns the string to used as operator between the two nested expressions.
     * <p>
     * (e.g. AND, XOR, +, -)
     * @return the operator keyword
     */
    protected abstract String getOperatorKeyword();


    /**
     * Creates an unary expression for the given bindable.
     * <p>
     * Add the enclosing parentheses, but not the parentheses for the operands.
     * @param bindable1 the first bindable object
     */
    protected Expression(Object bindable1) {
        this(bindable1, null);
    }
    
    /**
     * Creates an expression between two generic {@link SqlBindable}.
     * <p>
     * Add the enclosing parentheses, but not the parentheses for the operands.
     * @param bindable1 the first bindable object
     * @param bindable2 the second bindable object
     */
    protected Expression(Object bindable1,
                         Object bindable2) {
        this(bindable1, bindable2, true, false, false);
    }

    
    /**
     * Creates an expression between two generic {@link SqlBindable}.
     * @param bindable1 the first bindable object
     * @param bindable2 the second bindable object
     * @param enclosingParentheses whether use outer parentheses for build the SQL string
     * @param firstParentheses whether the first expression should be wrapped into parentheses
     * @param secondParentheses whether the first expression should be wrapped into parentheses
     */
    protected Expression(Object bindable1, 
                         Object bindable2, 
                         boolean enclosingParentheses, 
                         boolean firstParentheses,
                         boolean secondParentheses) {
        // We should not wrap null values in SqlBindables, instead we have to
        // assume that if a bindable is null then the expression is somehow unary.
        mBindable1 = bindable1 == null ? null : SqlBindableFactory.of(bindable1);
        mBindable2 = bindable2 == null ? null : SqlBindableFactory.of(bindable2);
        mOperatorKeyword = getOperatorKeyword();
        mEnclosingParentheses = enclosingParentheses;
        mFirstParentheses = firstParentheses;
        mSecondParentheses = secondParentheses;
    }
    
    

    // -------------------------------------------------------------------------
    // ---------------------------- ARITHMETIC ---------------------------------
    // -------------------------------------------------------------------------

    // +

    public Expression add(Object o) { return Operators.add(this, o); }

    // -

    public Expression sub(Object o) { return Operators.sub(this, o); }

    // *

    public Expression mul(Object o) { return Operators.mul(this, o); }

    // /

    public Expression div(Object o) { return Operators.div(this, o); }

    // %

    public Expression mod(Object o) { return Operators.mod(this, o); }

    // -------------------------------------------------------------------------
    // ---------------------------- COMPARISON ---------------------------------
    // -------------------------------------------------------------------------

    // =

    public Expression eq(Object o) { return Operators.eq(this, o); }

    // =

    public Expression neq(Object o) { return Operators.neq(this, o); }

    // >

    public Expression gt(Object o) { return Operators.gt(this, o); }

    // >=

    public Expression ge(Object o) { return Operators.ge(this, o); }

    // <

    public Expression lt(Object o) { return Operators.lt(this, o); }

    // <=

    public Expression le(Object o) { return Operators.le(this, o); }


    // -------------------------------------------------------------------------
    // ----------------------------- LOGICAL -----------------------------------
    // -------------------------------------------------------------------------

    // AND

    public Expression and(Object o) { return Operators.and(this, o); }

    // OR

    public Expression or(Object o) { return Operators.or(this, o); }

    // XOR

    public Expression xor(Object o) { return Operators.xor(this, o); }

    // LIKE

    public Expression like(Object o) { return Operators.like(this, o); }

    // BETWEEN

    public Expression between(Object o1, Object o2) { return Operators.between(this, o1, o2); }
    
    // IS NULL

    public Expression isNull() { return Operators.isNull(this); }

    // IS NOT NULL

    public Expression isNotNull() { return Operators.isNotNull(this); }

    // -------------------------------------------------------------------------
    // ----------------------------- BITWISE -----------------------------------
    // -------------------------------------------------------------------------

    // &
    
    public Expression bitAnd(Object o) { return Operators.bitAnd(this, o); }

    // |
    
    public Expression bitOr(Object o) { return Operators.bitOr(this, o); }

    // ^
    
    public Expression bitXor(Object o) { return Operators.bitXor(this, o); }

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
        return this
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
    }
}
