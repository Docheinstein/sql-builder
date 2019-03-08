package org.docheinstein.sqlbuilder.functions;

/**
 * Contains the method for create SQL functions.
 *
 **/
// WORK [NOT] IN PROGRESS...
public class Functions {

    public static class MySQL {

        // ADDTIME

        public static Addtime addtime(Object... params) { return new Addtime(params); }

        public static class Addtime extends Function {

            public Addtime(Object... params) {
                super(params);
            }

            @Override
            protected String getFunctionName() {
                return "ADDTIME";
            }
        }
    }
}
