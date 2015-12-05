package com.horn.common.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.logging.Logger;

/**
 * @author by lesinsa on 11.08.2015.
 */
public class LogbackToJULBridge extends AppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent event) {
        Logger logger = Logger.getLogger(event.getLoggerName());
        Level level = event.getLevel();
        java.util.logging.Level julLevel = takeLevel(level);
        if (logger.isLoggable(julLevel)) {
            logger.log(julLevel, event.getFormattedMessage());
        }
    }

    private java.util.logging.Level takeLevel(Level level) {
        java.util.logging.Level julLevel;
        switch (level.levelInt) {
            case Level.OFF_INT:
                julLevel = java.util.logging.Level.OFF;
                break;
            case Level.ERROR_INT:
                julLevel = java.util.logging.Level.SEVERE;
                break;
            case Level.WARN_INT:
                julLevel = java.util.logging.Level.WARNING;
                break;
            case Level.INFO_INT:
                julLevel = java.util.logging.Level.INFO;
                break;
            case Level.DEBUG_INT:
                julLevel = java.util.logging.Level.FINE;
                break;
            case Level.TRACE_INT:
                julLevel = java.util.logging.Level.FINEST;
                break;
            case Level.ALL_INT:
                julLevel = java.util.logging.Level.ALL;
                break;
            default:
                julLevel = java.util.logging.Level.ALL;
        }
        return julLevel;
    }
}
