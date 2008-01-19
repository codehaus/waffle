/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Mauro Talevi                                            *
 *****************************************************************************/
package org.codehaus.waffle.monitor;

/**
 * SilentMonitor is a writing monitor that writes nothing.
 * 
 * @author Mauro Talevi
 */
public class SilentMonitor extends AbstractWritingMonitor {

    protected void write(Level level, String message) {
        // write nothing
    }

    protected void trace(Throwable exception) {
        // write nothing
    }
}
