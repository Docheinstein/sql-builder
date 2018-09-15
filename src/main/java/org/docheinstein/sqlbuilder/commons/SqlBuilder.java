package org.docheinstein.sqlbuilder.commons;

public class SqlBuilder {

    private static SqlLanguage sLanguage = null;

    public static void setLanguage(SqlLanguage sqlLanguage) {
        sLanguage = sqlLanguage;
    }

    public static SqlLanguage getLanguage() {
        return sLanguage;
    }

    public static boolean isLanguageSet() {
        return sLanguage != null;
    }
}
