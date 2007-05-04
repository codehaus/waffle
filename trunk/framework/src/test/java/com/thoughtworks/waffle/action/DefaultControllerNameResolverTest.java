package com.thoughtworks.waffle.action;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import javax.servlet.http.HttpServletRequest;

public class DefaultControllerNameResolverTest extends MockObjectTestCase {

    public void testFindControllerName() {
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getPathInfo")
                .will(returnValue("/foo/bar.htm"));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        ControllerNameResolver controllerNameResolver = new DefaultControllerNameResolver();
        assertEquals("foo/bar", controllerNameResolver.findControllerName(request));
    }

    public void testFindControllerNameWithoutExtension() {
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getPathInfo")
                .will(returnValue("/foobar"));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        ControllerNameResolver controllerNameResolver = new DefaultControllerNameResolver();
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

        ControllerNameResolver controllerNameResolver = new DefaultControllerNameResolver();
        assertEquals("foobar", controllerNameResolver.findControllerName(request));
    }
}
