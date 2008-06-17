/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.monitor;

import static java.text.MessageFormat.format;

/**
 * Writing monitor that write events to console.  
 * 
 * @author Mauro Talevi
 */
public class ConsoleMonitor extends AbstractWritingMonitor {

    private static final String WAFFLE = "[WAFFLE]: {0} - {1}";

       @Override
    protected void write(Level level, String message) {
        System.out.println(format(WAFFLE, level, message));
    }

    @Override
    protected void trace(Throwable exception) {
        exception.printStackTrace();
    }

}
