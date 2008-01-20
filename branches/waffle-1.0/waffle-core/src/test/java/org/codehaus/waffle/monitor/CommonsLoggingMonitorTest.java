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

import static org.junit.Assert.assertNotNull;

import org.apache.commons.logging.Log;
import org.codehaus.waffle.monitor.Monitor.Level;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class CommonsLoggingMonitorTest {

    private final Mockery mockery = new Mockery();

    @Test
    public void canInstantiateUsingDefaultConstructor() {
        CommonsLoggingMonitor monitor = new CommonsLoggingMonitor();
        assertNotNull(monitor);
    }
    
    @Test
    public void canWriteWhenLoggingEnabled() {
        final Log log = mockery.mock(Log.class);
        final String expectedMessage = "[WAFFLE]: message";
        mockery.checking(new Expectations() {
            {
                one(log).isDebugEnabled(); will(returnValue(true));
                one(log).debug(expectedMessage);
                one(log).isErrorEnabled(); will(returnValue(true));
                one(log).error(expectedMessage);
                one(log).isInfoEnabled(); will(returnValue(true));
                one(log).info(expectedMessage);
                one(log).isWarnEnabled(); will(returnValue(true));
                one(log).warn(expectedMessage);
            }
        });

        String message = "message";
        final CommonsLoggingMonitor monitor = new CommonsLoggingMonitor(log);
        monitor.write(Level.DEBUG, message);
        monitor.write(Level.ERROR, message);
        monitor.write(Level.INFO, message);
        monitor.write(Level.WARN, message);
    }

    @Test
    public void cannotWriteWhenLoggingNotEnabled() {
        final Log log = mockery.mock(Log.class);
        final String message = "message";
        mockery.checking(new Expectations() {
            {
                one(log).isDebugEnabled(); will(returnValue(false));
                one(log).isErrorEnabled(); will(returnValue(false));
                one(log).isInfoEnabled(); will(returnValue(false));
                one(log).isWarnEnabled(); will(returnValue(false));
            }
        });

        final CommonsLoggingMonitor monitor = new CommonsLoggingMonitor(log);
        monitor.write(Level.DEBUG, message);
        monitor.write(Level.ERROR, message);
        monitor.write(Level.INFO, message);
        monitor.write(Level.WARN, message);
    }
}
