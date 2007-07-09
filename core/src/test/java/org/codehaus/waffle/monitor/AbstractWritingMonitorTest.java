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

import java.lang.reflect.Method;
import java.util.Set;

import org.codehaus.waffle.action.MethodDefinition;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class AbstractWritingMonitorTest {

    private final Mockery mockery = new JUnit4Mockery();

    @Test
    public void canWriteEvents() {
        final StringBuffer sb = new StringBuffer();
        final AbstractWritingMonitor monitor = new AbstractWritingMonitor() {

            @Override
            protected void write(MonitorLevel level, String message) {
                sb.append(message).append("\n");
            }

        };
        MethodDefinition methodDefinition = mockMethodDefinition();
        monitor.actionMethodFound(methodDefinition);
        monitor.defaultActionMethodCached(Object.class, methodDefinition);
        monitor.defaultActionMethodFound(methodDefinition);
        monitor.methodNameResolved("methodName", "methodKey", mockSet());
        monitor.pragmaticActionMethodFound(methodDefinition);
        assertEquals(5, sb.toString().split("\n").length);
    }

    private MethodDefinition mockMethodDefinition() {
        try {
            Method method = Object.class.getMethod("toString", (Class[]) null);
            return new MethodDefinition(method);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Set<String> mockSet() {
        return mockery.mock(Set.class);
    }

}
