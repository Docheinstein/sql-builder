package org.docheinstein.sqlbuilder.functions;

import org.docheinstein.sqlbuilder.SqlBindable;

/**
 * Contains the method for create SQL functions.
 *
 **/
// WORK IN PROGRESS...
public class Functions {

    public static class MySQL {

        public static Addtime addtime(SqlBindable... params) { return new Addtime(params); }

        public static class Addtime extends Function {

            public Addtime(SqlBindable... params) {
                super(params);
            }

            @Override
            protected String getFunctionName() {
                return "ADDTIME";
            }
        }
    }
}
