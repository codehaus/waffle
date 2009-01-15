package org.codehaus.waffle.view;

import static java.text.MessageFormat.format;
import static org.codehaus.waffle.view.DefaultViewDispatcher.ATTACHMENT_FILENAME;
import static org.codehaus.waffle.view.DefaultViewDispatcher.CONTENT_DISPOSITION_HEADER;
import static org.codehaus.waffle.view.DefaultViewDispatcher.LOCATION_HEADER;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.monitor.SilentMonitor;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
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

    @Test
    public void canDispatchExportView() throws IOException, ServletException {
        final String contentType = "text/csv";
        final String content = "1,2,3";
        final String filename = "export.csv";
        ExportView view = new ExportView(contentType, content.getBytes(), filename);
        ViewResolver viewResolver = mockViewResolver(view, PATH);

        mockery.checking(new Expectations() {{
            one(mockResponse).setContentType(contentType);
            one(mockResponse).setHeader(CONTENT_DISPOSITION_HEADER, format(ATTACHMENT_FILENAME,filename));
            one(mockResponse).getOutputStream();
            will(returnValue(mockOutputStream()));
        }});

        DefaultViewDispatcher viewDispatcher = new DefaultViewDispatcher(viewResolver, new SilentMonitor());
        viewDispatcher.dispatch(mockRequest, mockResponse, view);
    }

    @Test
    public void canDispatchViewOfTypeResponder() throws IOException, ServletException {
        SomeResponderView view = new SomeResponderView();

        ViewResolver viewResolver = mockViewResolver(view, PATH);

        DefaultViewDispatcher viewDispatcher = new DefaultViewDispatcher(viewResolver, new SilentMonitor());
        viewDispatcher.dispatch(mockRequest, mockResponse, view);
        assertTrue(view.isResponded());
    }
    
    @Test
    public void canDispatchRedirectView() throws IOException, ServletException {
        RedirectView redirectView = new RedirectView(PATH, null);
        ViewResolver viewResolver = mockViewResolver(redirectView, PATH);

        mockery.checking(new Expectations() {{
            one(mockResponse).setStatus(HttpServletResponse.SC_SEE_OTHER);
            one(mockResponse).setHeader(LOCATION_HEADER, PATH);
        }});

        DefaultViewDispatcher viewDispatcher = new DefaultViewDispatcher(viewResolver, new SilentMonitor());
        viewDispatcher.dispatch(mockRequest, mockResponse, redirectView);
    }

    @Test
    public void canDispatchStandardView() throws IOException, ServletException {
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
        mockery.checking(new Expectations() {
            {
                allowing(viewResolver).resolve(view);
                will(returnValue(path));
            }
        });
        return viewResolver;
    }

    private HttpServletRequest mockRequest() {
        return mockery.mock(HttpServletRequest.class);
    }

    private HttpServletResponse mockResponse() {
        return mockery.mock(HttpServletResponse.class);
    }

    private ServletOutputStream mockOutputStream() throws IOException {
       return new ServletOutputStream(){

        @Override
        public void write(int b) throws IOException {
        }
           
       };
    }

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


}
