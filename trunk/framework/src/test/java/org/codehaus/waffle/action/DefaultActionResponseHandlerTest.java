package org.codehaus.waffle.action;

import org.codehaus.waffle.view.View;
import org.codehaus.waffle.view.ViewDispatcher;
import org.codehaus.waffle.action.ActionMethodResponseHandler;
import org.codehaus.waffle.action.DefaultActionMethodResponseHandler;
import org.codehaus.waffle.action.ActionMethodResponse;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultActionResponseHandlerTest extends MockObjectTestCase {

    public void testConstructor() {
        try {
            new DefaultActionMethodResponseHandler(null);
            fail("IllegalArgumentException expected, null is not a valid argument");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    public void testHandleWhenResponseIsCommitted() throws Exception {
        // Mock HttpServletResponse
        Mock mockHttpServletResponse = mock(HttpServletResponse.class);
        mockHttpServletResponse.expects(once())
                .method("isCommitted")
                .will(returnValue(true));
        HttpServletResponse response = (HttpServletResponse) mockHttpServletResponse.proxy();

        // Mock ViewDispatcher
        Mock mockViewResolver = mock(ViewDispatcher.class);
        ViewDispatcher viewDispatcher = (ViewDispatcher) mockViewResolver.proxy();

        ActionMethodResponseHandler handler = new DefaultActionMethodResponseHandler(viewDispatcher);
        handler.handle(null, response, null);
    }

    public void testMethodResponseWrapsView() throws IOException, ServletException {
        View view = new View("foobar", null);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodResponse.setReturnValue(view);

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        mockResponse.expects(once())
                .method("isCommitted")
                .will(returnValue(false));
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock ViewDispatcher
        Mock mockViewResolver = mock(ViewDispatcher.class);
        mockViewResolver.expects(once()).method("dispatch");
        ViewDispatcher viewDispatcher = (ViewDispatcher) mockViewResolver.proxy();

        ActionMethodResponseHandler handler = new DefaultActionMethodResponseHandler(viewDispatcher);
        handler.handle(request, response, actionMethodResponse);
    }

    public void testDirectResponseActionMethod() throws Exception {
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodResponse.setReturnValue("Mmmmm Waffles!");

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        mockResponse.expects(once())
                .method("isCommitted")
                .will(returnValue(false));
        StubServletOutputStream out = new StubServletOutputStream();
        mockResponse.expects(once()).method("getOutputStream")
                .will(returnValue(out));
        mockResponse.expects(once()).method("flushBuffer");
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock ViewDispatcher
        Mock mockViewResolver = mock(ViewDispatcher.class);
        ViewDispatcher viewDispatcher = (ViewDispatcher) mockViewResolver.proxy();

        ActionMethodResponseHandler handler = new DefaultActionMethodResponseHandler(viewDispatcher);
        handler.handle(request, response, actionMethodResponse);

        assertEquals("Mmmmm Waffles!", out.buffer.toString());
    }

    public void testResponseIsAnException() throws IOException, ServletException {
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodResponse.setReturnValue(new Exception("error for testing"));

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        mockResponse.expects(once())
                .method("isCommitted")
                .will(returnValue(false));
        StubServletOutputStream out = new StubServletOutputStream();
        mockResponse.expects(once()).method("setStatus")
                .with(eq(HttpServletResponse.SC_BAD_REQUEST));
        mockResponse.expects(once()).method("getOutputStream")
                .will(returnValue(out));
        mockResponse.expects(once()).method("flushBuffer");
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock ViewDispatcher
        Mock mockViewResolver = mock(ViewDispatcher.class);
        ViewDispatcher viewDispatcher = (ViewDispatcher) mockViewResolver.proxy();

        ActionMethodResponseHandler handler = new DefaultActionMethodResponseHandler(viewDispatcher);
        handler.handle(request, response, actionMethodResponse);

        assertEquals("error for testing", out.buffer.toString());
    }

    public void handleNullMethodResponse() throws IOException, ServletException {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getHeader")
                .with(eq("referer"))
                .will(returnValue("foobar"));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        mockResponse.expects(once())
                .method("isCommitted")
                .will(returnValue(false));
        mockResponse.expects(once())
                .method("sendRedirect")
                .with(eq("foobar"));
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock ViewDispatcher
        Mock mockViewResolver = mock(ViewDispatcher.class);
        ViewDispatcher viewDispatcher = (ViewDispatcher) mockViewResolver.proxy();

        ActionMethodResponseHandler handler = new DefaultActionMethodResponseHandler(viewDispatcher);
        handler.handle(request, response, null);
    }

    private class StubServletOutputStream extends ServletOutputStream {
        public StringBuffer buffer = new StringBuffer();

        public void print(String string) throws IOException {
            buffer.append(string);
        }

        public void write(int b) throws IOException {

        }
    }
}
