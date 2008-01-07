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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * @author Mauro Talevi
 */
public class MonitorLevelTest {

    @Test
    public void enumsHHaveValidStringRepresentations() {
        assertEquals(MonitorLevel.DEBUG.toString(), "Debug");
        assertEquals(MonitorLevel.ERROR.toString(), "Error");
        assertEquals(MonitorLevel.INFO.toString(), "Info");
        assertEquals(MonitorLevel.WARN.toString(), "Warn");
    }

}