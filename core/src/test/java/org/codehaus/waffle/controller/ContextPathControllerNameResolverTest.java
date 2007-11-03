package org.codehaus.waffle.controller;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class ContextPathControllerNameResolverTest {

    private Mockery mockery = new Mockery();

    @Test
    public void canFindControllerName() {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getPathInfo();
                will(returnValue("/foo/bar.htm"));
            }
        });

        ControllerNameResolver controllerNameResolver = new ContextPathControllerNameResolver();
        assertEquals("foo/bar", controllerNameResolver.findControllerName(request));
    }

    @Test
    public void canFindControllerNameWithoutExtension() {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getPathInfo();
                will(returnValue("/foobar"));
            }
        });

        ControllerNameResolver controllerNameResolver = new ContextPathControllerNameResolver();
        assertEquals("foobar", controllerNameResolver.findControllerName(request));
    }

    @Test
    public void canFindControllerNameWhenPathInfoIsNull() {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getPathInfo();
                will(returnValue(null));
                one(request).getRequestURI();
                will(returnValue("/waffle/foobar.htm"));
                one(request).getContextPath();
                will(returnValue("/waffle"));
            }
        });

        ControllerNameResolver controllerNameResolver = new ContextPathControllerNameResolver();
        assertEquals("foobar", controllerNameResolver.findControllerName(request));
    }
}
