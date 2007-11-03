package org.codehaus.waffle.action;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.waffle.monitor.ActionMonitor;
import org.codehaus.waffle.monitor.SilentMonitor;
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
public class RequestParameterMethodNameResolverTest {
    
    private Mockery mockery = new Mockery();
    
    private ActionMonitor monitor = new SilentMonitor();

    @Test
    public void canResolve() {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getParameter("method");
                will(returnValue("foobar"));
                one(request).getParameterMap();
                will(returnValue(mockParameterMap("method")));
            }
        });

        MethodNameResolver resolver = new RequestParameterMethodNameResolver(monitor);
        assertEquals("foobar", resolver.resolve(request));
    }

    @Test
    public void canResolveWithAlternateConfiguration() {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getParameter("soda");
                will(returnValue("foobar"));
                one(request).getParameterMap();
                will(returnValue(mockParameterMap("soda")));
            }
        });

        RequestParameterMethodNameResolverConfig configuration = new RequestParameterMethodNameResolverConfig() {
            public String getMethodParameterKey() {
                return "soda";
            }
        };

        MethodNameResolver resolver = new RequestParameterMethodNameResolver(configuration, monitor);
        assertEquals("foobar", resolver.resolve(request));
    }
    
    @SuppressWarnings({"unchecked"})
    private Map mockParameterMap(final String name) {
        // Mock Map
        final Map map = mockery.mock(Map.class);
        mockery.checking(new Expectations() {
            {
                one(map).keySet();
                will(returnValue(new HashSet(Arrays.asList(new String[]{name}))));
            }
        });
        return map;
    }


}
