package org.codehaus.waffle.controller;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.codehaus.waffle.controller.ContextPathControllerNameResolver;
import org.codehaus.waffle.controller.ControllerNameResolver;

import javax.servlet.http.HttpServletRequest;

public class ContextPathControllerNameResolverTest extends MockObjectTestCase {

    public void testFindControllerName() {
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getPathInfo")
                .will(returnValue("/foo/bar.htm"));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        ControllerNameResolver controllerNameResolver = new ContextPathControllerNameResolver();
        assertEquals("foo/bar", controllerNameResolver.findControllerName(request));
    }

    public void testFindControllerNameWithoutExtension() {
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getPathInfo")
                .will(returnValue("/foobar"));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        ControllerNameResolver controllerNameResolver = new ContextPathControllerNameResolver();
        assertEquals("foobar", controllerNameResolver.findControllerName(request));
    }

    public void testFindControllerNameWhenPathInfoIsNull() {
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getPathInfo")
                .will(returnValue(null));
        mockRequest.expects(once())
                .method("getRequestURI")
                .will(returnValue("/waffle/foobar.htm"));
        mockRequest.expects(once())
                .method("getContextPath")
                .will(returnValue("/waffle"));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        ControllerNameResolver controllerNameResolver = new ContextPathControllerNameResolver();
        assertEquals("foobar", controllerNameResolver.findControllerName(request));
    }
}
