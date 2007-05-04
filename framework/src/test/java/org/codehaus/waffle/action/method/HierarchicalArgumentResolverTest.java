package org.codehaus.waffle.action.method;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HierarchicalArgumentResolverTest extends MockObjectTestCase {

    public void testResolveReturnsNameWhenArgumentNotInCurlyBrackets() {
        ArgumentResolver argumentResolver = new HierarchicalArgumentResolver(null);
        assertEquals("foobar", argumentResolver.resolve(null, "foobar"));
    }

    public void testResolveFoundInParameter() {
        // Mock Request
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getParameter")
                .with(eq("foo"))
                .will(returnValue("bar"));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        ArgumentResolver argumentResolver = new HierarchicalArgumentResolver(null);
        assertEquals("bar", argumentResolver.resolve(request, "{foo}"));
    }

    public void testResolveFoundInRequestAttribute() {
        // Mock Request
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getParameter")
                .with(eq("foo"))
                .will(returnValue(null));
        mockRequest.expects(once())
                .method("getAttribute")
                .with(eq("foo"))
                .will(returnValue("bar"));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        ArgumentResolver argumentResolver = new HierarchicalArgumentResolver(null);
        assertEquals("bar", argumentResolver.resolve(request, "{foo}"));
    }

    public void testResolveFoundInSessionAttribute() {
        // Mock HttpSession
        Mock mockSession = mock(HttpSession.class);
        mockSession.expects(once())
                .method("getAttribute")
                .with(eq("foo"))
                .will(returnValue("bar"));
        HttpSession session = (HttpSession) mockSession.proxy();

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getParameter")
                .with(eq("foo"))
                .will(returnValue(null));
        mockRequest.expects(once())
                .method("getAttribute")
                .with(eq("foo"))
                .will(returnValue(null));
        mockRequest.expects(once())
                .method("getSession")
                .will(returnValue(session));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        ArgumentResolver argumentResolver = new HierarchicalArgumentResolver(null);
        assertEquals("bar", argumentResolver.resolve(request, "{foo}"));
    }

    public void testResolveFoundInServletContext() {
        Mock mockServletContext = mock(ServletContext.class);
        mockServletContext.expects(once())
                .method("getAttribute")
                .with(eq("foo"))
                .will(returnValue("bar"));
        ServletContext servletContext = (ServletContext) mockServletContext.proxy();

        // Mock HttpSession
        Mock mockSession = mock(HttpSession.class);
        mockSession.expects(once())
                .method("getAttribute")
                .with(eq("foo"))
                .will(returnValue(null));
        HttpSession session = (HttpSession) mockSession.proxy();

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getParameter")
                .with(eq("foo"))
                .will(returnValue(null));
        mockRequest.expects(once())
                .method("getAttribute")
                .with(eq("foo"))
                .will(returnValue(null));
        mockRequest.expects(once())
                .method("getSession")
                .will(returnValue(session));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        ArgumentResolver argumentResolver = new HierarchicalArgumentResolver(servletContext);
        assertEquals("bar", argumentResolver.resolve(request, "{foo}"));
    }

    public void testResolveFoundInServletContextWhenSessionIsNull() {
        Mock mockServletContext = mock(ServletContext.class);
        mockServletContext.expects(once())
                .method("getAttribute")
                .with(eq("foo"))
                .will(returnValue("bar"));
        ServletContext servletContext = (ServletContext) mockServletContext.proxy();

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getParameter")
                .with(eq("foo"))
                .will(returnValue(null));
        mockRequest.expects(once())
                .method("getAttribute")
                .with(eq("foo"))
                .will(returnValue(null));
        mockRequest.expects(once())
                .method("getSession")
                .will(returnValue(null));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        ArgumentResolver argumentResolver = new HierarchicalArgumentResolver(servletContext);
        assertEquals("bar", argumentResolver.resolve(request, "{foo}"));
    }
}
