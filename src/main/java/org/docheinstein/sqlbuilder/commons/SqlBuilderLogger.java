package org.docheinstein.sqlbuilder.commons;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Library's logger for debug purpose
 */
public class SqlBuilderLogger {

    /** Listener of this logger's messages. */
    public interface LoggerListener {

        /** Called when a message is delivered from this library. */
        void onLoggerMessage(String message);
    }

    /** Whether the logger is currently enabled. */
    private static boolean sEnabled = false;

    /** The listeners of this logger. */
    private static final Set<LoggerListener> sListeners = new CopyOnWriteArraySet<>();

    /**
     * Enables or disabled the logger.
     * <p>
     * If the logger is not enabled the {@link #out(String)} method won't be called.
     * @param enabled whether the logger is enabled
     */
    public static void enable(boolean enabled) {
        sEnabled = enabled;
    }

    /**
     * Adds a listener that will be notified about new message.
     * @param ll the logger listener
     */
    public static void addListener(LoggerListener ll) {
        sListeners.add(ll);
    }

    /**
     * Removes a listener from the listeners set.
     * @param ll the logger listener
     */
    public static void removeListener(LoggerListener ll) {
        sListeners.remove(ll);
    }

    /**
     * Notifies the listeners about a new message.
     * @param str the message
     */
    public static void out(String str) {
        if (sEnabled)
            sListeners.forEach(l -> l.onLoggerMessage(str));
    }
}
