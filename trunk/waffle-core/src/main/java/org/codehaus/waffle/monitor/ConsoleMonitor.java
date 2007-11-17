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
