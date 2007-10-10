package org.codehaus.waffle.action;

import org.codehaus.waffle.monitor.ActionMonitor;
import org.codehaus.waffle.testmodel.StubMonitor;
import org.codehaus.waffle.testmodel.StubViewDispatcher;
import org.codehaus.waffle.view.View;
import org.codehaus.waffle.view.ViewDispatcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RunWith(JMock.class)
public class DefaultActionMethodResponseHandlerTest {
    private final Mockery context = new JUnit4Mockery();

    @Test
    public void constructorShouldsNotAcceptNulls() {
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
    public void executeShouldNotProcessResponseValueWhenResponseHasBeenCommitted() throws Exception {
        final HttpServletResponse response = context.mock(HttpServletResponse.class);
        context.checking(new Expectations() {{
            one (response).isCommitted();
            will(returnValue(true));
        }});

        ViewDispatcher viewDispatcher = context.mock(ViewDispatcher.class);
        ActionMonitor actionMonitor = context.mock(ActionMonitor.class);

        ActionMethodResponseHandler handler = new DefaultActionMethodResponseHandler(viewDispatcher, actionMonitor);
        handler.handle(null, response, null);
    }

    @Test
    public void responseValueOfTypeViewShouldBeDelegatedToViewDispatcher() throws IOException, ServletException {
        final View view = new View("foobar", null);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodResponse.setReturnValue(view);

        final HttpServletResponse response = context.mock(HttpServletResponse.class);
        context.checking(new Expectations() {{
            one (response).isCommitted();
            will(returnValue(false));
        }});

        final HttpServletRequest request = context.mock(HttpServletRequest.class);
        final ViewDispatcher viewDispatcher = context.mock(ViewDispatcher.class);
        context.checking(new Expectations() {{
            one (viewDispatcher).dispatch(request, response, view);
        }});

        ActionMonitor actionMonitor = context.mock(ActionMonitor.class);

        ActionMethodResponseHandler handler = new DefaultActionMethodResponseHandler(viewDispatcher, actionMonitor);
        handler.handle(request, response, actionMethodResponse);
    }

    @Test
    public void responseValueShouldBeWrittenToOutputStream() throws Exception {
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodResponse.setReturnValue("Mmmmm Waffles!");

        final StubServletOutputStream out = new StubServletOutputStream();

        final HttpServletResponse response = context.mock(HttpServletResponse.class);
        context.checking(new Expectations() {{
            one (response).isCommitted();
            will(returnValue(false));
            one(response).getOutputStream();
            will(returnValue(out));
            one(response).flushBuffer();
        }});

        HttpServletRequest request = context.mock(HttpServletRequest.class);
        ViewDispatcher viewDispatcher = context.mock(ViewDispatcher.class);
        ActionMonitor actionMonitor = context.mock(ActionMonitor.class);

        ActionMethodResponseHandler handler = new DefaultActionMethodResponseHandler(viewDispatcher, actionMonitor);
        handler.handle(request, response, actionMethodResponse);

        Assert.assertEquals("Mmmmm Waffles!", out.buffer.toString());
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    @Test
    public void responseValueOfTypeActionMethodExceptionShouldSetResponseCorrectly() throws IOException, ServletException {
        final Exception exception = new ActionMethodException(1985, "my message");
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        actionMethodResponse.setReturnValue(exception);
        final StubServletOutputStream out = new StubServletOutputStream();

        HttpServletRequest request = context.mock(HttpServletRequest.class);

        final HttpServletResponse response = context.mock(HttpServletResponse.class);
        context.checking(new Expectations() {{
            one (response).isCommitted();
            will(returnValue(false));
            one(response).setStatus(1985);
            one(response).getOutputStream();
            will(returnValue(out));
            one(response).flushBuffer();
        }});

        ViewDispatcher viewDispatcher = context.mock(ViewDispatcher.class);
        final ActionMonitor actionMonitor = context.mock(ActionMonitor.class);
        context.checking(new Expectations() {{
            one (actionMonitor).actionMethodExecutionFailed(exception);
        }});

        // must fire the exception to the monitor
        ActionMethodResponseHandler handler = new DefaultActionMethodResponseHandler(viewDispatcher, actionMonitor);
        handler.handle(request, response, actionMethodResponse);

        Assert.assertEquals("my message", out.buffer.toString());
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
