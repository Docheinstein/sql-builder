package org.docheinstein.sqlbuilder.functions;

import org.docheinstein.sqlbuilder.SqlBindable;
import org.docheinstein.sqlbuilder.Sqlable;
import org.docheinstein.sqlbuilder.commons.SqlBindableFactory;
import org.docheinstein.sqlbuilder.commons.SqlBuilderInternalUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a function which has a name and takes an arbitrary numbers
 * of parameters.
 *
 * e.g. ADDTIME(Datetime, Time)
 */
public abstract class Function implements SqlBindable {

    /** The name of the function. */
    protected String mFunctionName;

    /**
     * Returns the function name.
     * @return the function name
     */
    protected abstract String getFunctionName();

    /**
     * The parameters of the functions, which are treated as sql bindables.
     */
    protected List<SqlBindable> mParameters = new ArrayList<>();

    /**
     * Creates a function with the given parameters.
     * @param params the params of the function
     */
    protected Function(Object... params) {
        mFunctionName = getFunctionName();
        for (Object o : params)
            mParameters.add(SqlBindableFactory.of(o));
    }

    @Override
    public List<Object> getBindableObjects() {
        List<Object> bindables = new ArrayList<>();

        for (SqlBindable param  : mParameters) {
            List<Object> paramBindables = param.getBindableObjects();
            if (paramBindables != null)
                bindables.addAll(paramBindables);
        }

        return bindables;
    }

    @Override
    public String toSql() {
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        sb.append(mFunctionName);
        sb.append("(");
        sb.append(SqlBuilderInternalUtil.getAsCommaList(
            mParameters,
            Sqlable::toSql)
        );
        sb.append(") ");

        return sb.toString();
    }
}
