package com.thoughtworks.waffle.view;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class DefaultViewDispatcherTest extends MockObjectTestCase {

    public void testDispatchCalled() throws IOException, ServletException {
        Map model = new HashMap();
        RedirectView redirectView = new RedirectView("/foobar.html", null, model);

        // Mock ViewResolver
        Mock mockViewResolver = mock(ViewResolver.class);
        mockViewResolver.expects(once())
                .method("resolve")
                .with(eq(redirectView))
                .will(returnValue("/foobar.html"));
        ViewResolver viewResolver = (ViewResolver) mockViewResolver.proxy();

        // Mock DispatchAssistant
        Mock mockDispatchHelper = mock(DispatchAssistant.class);
        mockDispatchHelper.expects(once())
                .method("redirect")
                .with(ANYTHING, ANYTHING, same(model), eq("/foobar.html"));
        DispatchAssistant dispatchAssistant = (DispatchAssistant) mockDispatchHelper.proxy();

        DefaultViewDispatcher viewDispatcher = new DefaultViewDispatcher(viewResolver, dispatchAssistant);
        viewDispatcher.dispatch(null, null, redirectView);
    }

    public void testForwardCalled() throws IOException, ServletException {
        View view = new View("/foobar.html", null);

        // Mock ViewResolver
        Mock mockViewResolver = mock(ViewResolver.class);
        mockViewResolver.expects(once())
                        .method("resolve")
                        .with(eq(view))
                        .will(returnValue("/foobar.html"));
        ViewResolver viewResolver = (ViewResolver) mockViewResolver.proxy();

        // Mock DispatchAssistant
        Mock mockDispatchHelper = mock(DispatchAssistant.class);
        mockDispatchHelper.expects(once())
                .method("forward")
                .with(ANYTHING, ANYTHING, eq("/foobar.html"));
        DispatchAssistant dispatchAssistant = (DispatchAssistant) mockDispatchHelper.proxy();

        DefaultViewDispatcher viewDispatcher = new DefaultViewDispatcher(viewResolver, dispatchAssistant);
        viewDispatcher.dispatch(null, null, view);
    }
}
