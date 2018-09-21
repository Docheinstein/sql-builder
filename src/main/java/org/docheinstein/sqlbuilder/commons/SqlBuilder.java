package org.docheinstein.sqlbuilder.commons;

/**
 * Main class of the project.
 * <p>
 * Actually this is used only for set the language via {@link #setLanguage(SqlLanguage)},
 * which must be done for use specific language statements like
 * {@link org.docheinstein.sqlbuilder.statements.mysql.CreateTriggerMySQL}.
 */
public class SqlBuilder {

    /** Language used by specific language statements. */
    private static SqlLanguage sLanguage = null;

    /**
     * Sets the global SQL language used by specific language statements.
     * @param sqlLanguage the language to use
     */
    public static void setLanguage(SqlLanguage sqlLanguage) {
        SqlBuilderLogger.out("SQL language set to: " + sqlLanguage);
        sLanguage = sqlLanguage;
    }

    /**
     * Returns the language set via {@link #setLanguage(SqlLanguage)}.
     * @return the current sql language
     */
    public static SqlLanguage getLanguage() {
        return sLanguage;
    }

    /**
     * Returns true if the SQL language has been set via {@link #setLanguage(SqlLanguage)}.
     * @return true if the current sql language is not null
     */
    public static boolean isLanguageSet() {
        return sLanguage != null;
    }
}
