package com.thoughtworks.waffle.action.method;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import javax.servlet.http.HttpServletRequest;

public class RequestParameterMethodNameResolverTest extends MockObjectTestCase {

    public void testResolve() {
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getParameter")
                .with(eq("method"))
                .will(returnValue("foobar"));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        MethodNameResolver resolver = new RequestParameterMethodNameResolver();
        assertEquals("foobar", resolver.resolve(request));
    }

    public void testResolveWithAlternateConfiguration() {
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getParameter")
                .with(eq("soda"))
                .will(returnValue("foobar"));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        RequestParameterMethodNameResolverConfig configuration = new RequestParameterMethodNameResolverConfig() {
            public String getMethodParameterKey() {
                return "soda";
            }
        };

        MethodNameResolver resolver = new RequestParameterMethodNameResolver(configuration);
        assertEquals("foobar", resolver.resolve(request));
    }
}
