package org.docheinstein.sqlbuilder.commons;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class SqlBuilderLogger {
    public interface LoggerListener {
        void onLoggerMessage(String message);
    }

    private static boolean sEnabled = false;

    private static final Set<LoggerListener> sListeners = new CopyOnWriteArraySet<>();

    public static void addListener(LoggerListener ll) {
        sListeners.add(ll);
    }

    public static void removeListener(LoggerListener ll) {
        sListeners.remove(ll);
    }

    public static void enable(boolean enabled) {
        sEnabled = enabled;
    }

    public static void out(String str) {
        if (sEnabled)
            sListeners.forEach(l -> l.onLoggerMessage(str));
    }
}
