package org.codehaus.waffle.action;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.waffle.monitor.Monitor;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class RequestParameterMethodNameResolverTest extends MockObjectTestCase {

    private Monitor monitor = new SilentMonitor();

    public void testResolve() {
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getParameter")
                .with(eq("method"))
                .will(returnValue("foobar"));
        mockRequest.expects(once())
                .method("getParameterMap")
                .will(returnValue(mockParameterMap("method")));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        MethodNameResolver resolver = new RequestParameterMethodNameResolver(monitor);
        assertEquals("foobar", resolver.resolve(request));
    }

    public void testResolveWithAlternateConfiguration() {
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getParameter")
                .with(eq("soda"))
                .will(returnValue("foobar"));
        mockRequest.expects(once())
                .method("getParameterMap")
                .will(returnValue(mockParameterMap("soda")));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        RequestParameterMethodNameResolverConfig configuration = new RequestParameterMethodNameResolverConfig() {
            public String getMethodParameterKey() {
                return "soda";
            }
        };

        MethodNameResolver resolver = new RequestParameterMethodNameResolver(configuration, monitor);
        assertEquals("foobar", resolver.resolve(request));
    }
    
    private Map mockParameterMap(String name) {
        Mock mockMap = mock(Map.class);
        mockMap.expects(once())
                .method("keySet")
                .will(returnValue(new HashSet(Arrays.asList(new String[]{name}))));
        return (Map) mockMap.proxy();
    }


}
