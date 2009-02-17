/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.monitor;

import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Set;

import org.codehaus.waffle.action.MethodDefinition;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class AbstractWritingMonitorTest {

    private final Mockery mockery = new Mockery();

    @Test
    public void canWriteMessages() {
        final StringBuffer sb = new StringBuffer();
        final AbstractWritingMonitor monitor = new AbstractWritingMonitor() {

            @Override
            protected void write(Level level, String message) {
                sb.append(message).append("\n");
            }

            @Override
            protected void trace(Throwable exception) {
                // will not be tested here
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

    @Test
    public void canTraceExceptions() {
        final StringWriter monitorWriter = new StringWriter();
        final AbstractWritingMonitor monitor = new AbstractWritingMonitor() {
            @Override
            protected void write(Level level, String message) {
                // will not be tested here
            }

            @Override
            protected void trace(Throwable exception) {
                exception.printStackTrace(new PrintWriter(monitorWriter));
            }
        };
        Exception exception = new Exception();
        StackTraceElement[] elements = {
                new StackTraceElement("MyController", "myPrivateMethod", "package.MyController.java", 10),
                new StackTraceElement("MyController", "myActionMethod", "package.MyController.java", 1),
        };
        exception.setStackTrace(elements);
        monitor.trace(exception);

        StringWriter expectedMessageWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(expectedMessageWriter));

        assertEquals(expectedMessageWriter.toString(), monitorWriter.toString());
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
