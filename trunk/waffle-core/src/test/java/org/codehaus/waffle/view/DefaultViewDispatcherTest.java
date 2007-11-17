package org.codehaus.waffle.view;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.monitor.SilentMonitor;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Michael Ward
 * @author Mauro Talevi
 * @author Paulo Silveira
 */
@RunWith(JMock.class)
public class DefaultViewDispatcherTest {
    private static final String PATH = "/foobar.html";
    private final Mockery mockery = new Mockery();
    private final HttpServletRequest mockRequest = mockRequest();
    private final HttpServletResponse mockResponse = mockResponse();

    class SomeResponderView extends ResponderView {

        private boolean responded = false;

        @Override
        public void respond(ServletRequest req, HttpServletResponse resp) throws IOException {
            responded = true;
        }

        public boolean isResponded() {
            return responded;
        }
    }

    @Test
    public void dispatchShouldCallRespondIfViewIsOfTypeResponder() throws IOException, ServletException {
        SomeResponderView view = new SomeResponderView();

        ViewResolver viewResolver = mockViewResolver(view, PATH);

        DefaultViewDispatcher viewDispatcher = new DefaultViewDispatcher(viewResolver, new SilentMonitor());
        viewDispatcher.dispatch(mockRequest, mockResponse, view);
        Assert.assertTrue(view.isResponded());
    }

    @Test
    public void dispatchShouldHandleRedirectView() throws IOException, ServletException {
        RedirectView redirectView = new RedirectView(PATH, null);
        ViewResolver viewResolver = mockViewResolver(redirectView, PATH);

        mockery.checking(new Expectations() {{
            one(mockResponse).setStatus(HttpServletResponse.SC_SEE_OTHER);
            one(mockResponse).setHeader("Location", PATH);
        }});

        DefaultViewDispatcher viewDispatcher = new DefaultViewDispatcher(viewResolver, new SilentMonitor());
        viewDispatcher.dispatch(mockRequest, mockResponse, redirectView);
    }

    @Test
    public void dispatchShouldHandleStandardView() throws IOException, ServletException {
        View view = new View(PATH, null);
        ViewResolver viewResolver = mockViewResolver(view, PATH);

        final RequestDispatcher requestDispatcher = mockery.mock(RequestDispatcher.class);

        mockery.checking(new Expectations() {{
            one(mockRequest).getRequestDispatcher(PATH);
            will(returnValue(requestDispatcher));
            one(requestDispatcher).forward(mockRequest, mockResponse);
        }});

        DefaultViewDispatcher viewDispatcher = new DefaultViewDispatcher(viewResolver, new SilentMonitor());
        viewDispatcher.dispatch(mockRequest, mockResponse, view);
    }

    private ViewResolver mockViewResolver(final View view, final String path) {
        final ViewResolver viewResolver = mockery.mock(ViewResolver.class);
        Expectations expectations = new Expectations() {
            {
                allowing(viewResolver).resolve(view);
                will(returnValue(path));
            }
        };
        mockery.checking(expectations);
        return viewResolver;
    }

    private HttpServletResponse mockResponse() {
        return mockery.mock(HttpServletResponse.class);
    }

    private HttpServletRequest mockRequest() {
        return mockery.mock(HttpServletRequest.class);
    }

}
