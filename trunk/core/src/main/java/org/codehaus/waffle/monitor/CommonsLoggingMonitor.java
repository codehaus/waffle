/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Mauro Talevi                                            *
 *****************************************************************************/
package org.codehaus.waffle.monitor;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Writing monitor that uses Commons-Logging to log events
 * 
 * @author Mauro Talevi
 */
public class CommonsLoggingMonitor extends AbstractWritingMonitor {

    private static final String WAFFLE_TEMPLATE = "[WAFFLE]: {0}";
    private final Log log;

    /**
     * Creates the default CommonsLoggingMonitor
     */
    public CommonsLoggingMonitor() {
        this(CommonsLoggingMonitor.class.getName());
    }

    /**
     * Creates a CommonsLoggingMonitor with a given Log instance name. It uses the {@link LogFactory LogFactory} to
     * create the Log instance
     * 
     * @param logName the name of the Log
     */
    public CommonsLoggingMonitor(String logName) {
        this(LogFactory.getLog(logName));
    }

    /**
     * Creates a CommonsLoggingMonitor with a given Log instance
     * 
     * @param log the Log to write to
     */
    public CommonsLoggingMonitor(Log log) {
        this.log = log;
    }

    @Override
    protected void write(Level level, String message) {
        switch (level) {
            case ERROR:
                if (log.isErrorEnabled()) {
                    log.error(format(message));
                }
                break;
            case INFO:
                if (log.isInfoEnabled()) {
                    log.info(format(message));
                }
                break;
            case WARN:
                if (log.isWarnEnabled()) {
                    log.warn(format(message));
                }
                break;
            case DEBUG:
                if (log.isDebugEnabled()) {
                    log.debug(format(message));
                }
                break;
        }
    }

    @Override
    protected void trace(Throwable exception) {
        if (log.isErrorEnabled()) {
            log.error(format(exception.getMessage()), exception);
        }
    }

    private String format(String message) {
        return MessageFormat.format(WAFFLE_TEMPLATE, message);
    }

}
