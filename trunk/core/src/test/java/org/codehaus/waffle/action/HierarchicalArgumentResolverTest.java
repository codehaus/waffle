package org.codehaus.waffle.action;

import static org.junit.Assert.assertEquals;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
public class HierarchicalArgumentResolverTest {

    private Mockery mockery = new Mockery();

    @Test
    public void testResolveReturnsNameWhenArgumentNotInCurlyBrackets() {
        ArgumentResolver argumentResolver = new HierarchicalArgumentResolver(null);
        assertEquals("foobar", argumentResolver.resolve(null, "foobar"));
    }

    @Test
    public void canResolveArgumentFoundInParameter() {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getParameter("foo");
                will(returnValue("bar"));
            }
        });

        ArgumentResolver argumentResolver = new HierarchicalArgumentResolver(null);
        assertEquals("bar", argumentResolver.resolve(request, "{foo}"));
    }

    @Test
    public void canResolveArgumentFoundInAttribute() {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getParameter("foo");
                will(returnValue(null));
                one(request).getAttribute("foo");
                will(returnValue("bar"));
            }
        });

        ArgumentResolver argumentResolver = new HierarchicalArgumentResolver(null);
        assertEquals("bar", argumentResolver.resolve(request, "{foo}"));
    }

    @Test
    public void canResolveArgumentFoundInSessionAttribute() {
        // Mock HttpSession
        final HttpSession session = mockery.mock(HttpSession.class);
        mockery.checking(new Expectations() {
            {
                one(session).getAttribute("foo");
                will(returnValue("bar"));
            }
        });

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getParameter("foo");
                will(returnValue(null));
                one(request).getAttribute("foo");
                will(returnValue(null));
                one(request).getSession();
                will(returnValue(session));
            }
        });
        ArgumentResolver argumentResolver = new HierarchicalArgumentResolver(null);
        assertEquals("bar", argumentResolver.resolve(request, "{foo}"));
    }

    @Test
    public void canResolveArgumentFoundInServletContext() {
        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {
            {
                one(servletContext).getAttribute("foo");
                will(returnValue("bar"));
            }
        });

        // Mock HttpSession
        final HttpSession session = mockery.mock(HttpSession.class);
        mockery.checking(new Expectations() {
            {
                one(session).getAttribute("foo");
                will(returnValue(null));
            }
        });

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getParameter("foo");
                will(returnValue(null));
                one(request).getAttribute("foo");
                will(returnValue(null));
                one(request).getSession();
                will(returnValue(session));
            }
        });

        ArgumentResolver argumentResolver = new HierarchicalArgumentResolver(servletContext);
        assertEquals("bar", argumentResolver.resolve(request, "{foo}"));
    }

    @Test
    public void canResolveArgumentFoundInServletContextWhenSessionIsNull() {
        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {
            {
                one(servletContext).getAttribute("foo");
                will(returnValue("bar"));
            }
        });

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getParameter("foo");
                will(returnValue(null));
                one(request).getAttribute("foo");
                will(returnValue(null));
                one(request).getSession();
                will(returnValue(null));
            }
        });

        ArgumentResolver argumentResolver = new HierarchicalArgumentResolver(servletContext);
        assertEquals("bar", argumentResolver.resolve(request, "{foo}"));
    }
}
