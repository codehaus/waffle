package org.codehaus.waffle.action;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.monitor.ActionMonitor;
import org.codehaus.waffle.testmodel.StubMonitor;
import org.codehaus.waffle.testmodel.StubViewDispatcher;
import org.codehaus.waffle.view.View;
import org.codehaus.waffle.view.ViewDispatcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class DefaultActionMethodResponseHandlerTest {
    private final Mockery mockery = new Mockery();

    @Test
    public void cannotAcceptNullsInConstructor() {
        try {
            new DefaultActionMethodResponseHandler(null, null);
            Assert.fail("IllegalArgumentException expected, null is not a valid argument");
        } catch (IllegalArgumentException expected) {
            // expected
        }
        try {
            new DefaultActionMethodResponseHandler(null, new StubMonitor());
            Assert.fail("IllegalArgumentException expected, null is not a valid argument");
        } catch (IllegalArgumentException expected) {
            // expected
        }
        try {
            new DefaultActionMethodResponseHandler(new StubViewDispatcher(), null);
            Assert.fail("IllegalArgumentException expected, null is not a valid argument");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void canAvoidProcessingResponseValueWhenResponseHasBeenCommitted() throws Exception {
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);
        mockery.checking(new Expectations() {{
            one (response).isCommitted();
            will(returnValue(true));
        }});

        ViewDispatcher viewDispatcher = mockery.mock(ViewDispatcher.class);
        ActionMonitor actionMonitor = mockery.mock(ActionMonitor.class);

        ActionMethodResponseHandler handler = new DefaultActionMethodResponseHandler(viewDispatcher, actionMonitor);
        handler.handle(null, response, null);
    }

    @Test
    public void canDelegateResponseValueOfTypeViewToDispatcher() throws IOException, ServletException {
        final View view = new View("foobar", null);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodResponse.setReturnValue(view);

        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);
        mockery.checking(new Expectations() {{
            one (response).isCommitted();
            will(returnValue(false));
        }});

        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        final ViewDispatcher viewDispatcher = mockery.mock(ViewDispatcher.class);
        mockery.checking(new Expectations() {{
            one (viewDispatcher).dispatch(request, response, view);
        }});

        ActionMonitor actionMonitor = mockery.mock(ActionMonitor.class);

        ActionMethodResponseHandler handler = new DefaultActionMethodResponseHandler(viewDispatcher, actionMonitor);
        handler.handle(request, response, actionMethodResponse);
    }

    @Test
    public void canWriteResponseValueToOutputStream() throws Exception {
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodResponse.setReturnValue("Mmmmm Waffles!");

        final StubServletOutputStream out = new StubServletOutputStream();

        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);
        mockery.checking(new Expectations() {{
            one (response).isCommitted();
            will(returnValue(false));
            one(response).getOutputStream();
            will(returnValue(out));
            one(response).flushBuffer();
        }});

        HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        ViewDispatcher viewDispatcher = mockery.mock(ViewDispatcher.class);
        ActionMonitor actionMonitor = mockery.mock(ActionMonitor.class);

        ActionMethodResponseHandler handler = new DefaultActionMethodResponseHandler(viewDispatcher, actionMonitor);
        handler.handle(request, response, actionMethodResponse);

        assertEquals("Mmmmm Waffles!", out.buffer.toString());
    }

    @Test
    public void canHandleValueOfTypeActionMethodException() throws IOException, ServletException {
        final Exception exception = new ActionMethodException(1985, "my message");
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodResponse.setReturnValue(exception);
        final StubServletOutputStream out = new StubServletOutputStream();

        HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);
        mockery.checking(new Expectations() {{
            one (response).isCommitted();
            will(returnValue(false));
            one(response).setStatus(1985);
            one(response).getOutputStream();
            will(returnValue(out));
            one(response).flushBuffer();
        }});

        ViewDispatcher viewDispatcher = mockery.mock(ViewDispatcher.class);
        final ActionMonitor actionMonitor = mockery.mock(ActionMonitor.class);
        mockery.checking(new Expectations() {{
            one (actionMonitor).actionMethodExecutionFailed(exception);
        }});

        // must fire the exception to the monitor
        ActionMethodResponseHandler handler = new DefaultActionMethodResponseHandler(viewDispatcher, actionMonitor);
        handler.handle(request, response, actionMethodResponse);

        assertEquals("my message", out.buffer.toString());
    }

    private class StubServletOutputStream extends ServletOutputStream {
        public StringBuffer buffer = new StringBuffer();

        public void print(String string) throws IOException {
            buffer.append(string);
        }

        public void write(int b) throws IOException {
            // do nothing
        }
    }
}
