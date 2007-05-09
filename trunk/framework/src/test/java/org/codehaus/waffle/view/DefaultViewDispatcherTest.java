package org.codehaus.waffle.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class DefaultViewDispatcherTest {
    private static final String PATH = "/foobar.html";
    private Mockery mockery = new Mockery();
    private HttpServletRequest mockRequest = mockRequest();
    private HttpServletResponse mockResponse = mockResponse();

    @Test
    public void testDispatchCalled() throws IOException, ServletException {
        Map model = new HashMap();
        RedirectView redirectView = new RedirectView(PATH, null, model);
        ViewResolver viewResolver = mockViewResolver(redirectView, PATH);
        DispatchAssistant dispatchAssistant = mockDispatchAssistant(model, PATH);

        DefaultViewDispatcher viewDispatcher = new DefaultViewDispatcher(viewResolver, dispatchAssistant);
        viewDispatcher.dispatch(mockRequest, mockResponse, redirectView);
    }

    @Test
    public void testForwardCalled() throws IOException, ServletException {
        View view = new View(PATH, null);
        ViewResolver viewResolver = mockViewResolver(view, PATH);
        DispatchAssistant dispatchAssistant = mockDispatchAssistant(null, PATH);

        DefaultViewDispatcher viewDispatcher = new DefaultViewDispatcher(viewResolver, dispatchAssistant);
        viewDispatcher.dispatch(mockRequest, mockResponse, view);
    }

    private DispatchAssistant mockDispatchAssistant(final Map model, final String path) throws IOException, ServletException {
        final DispatchAssistant dispatchAssistant = mockery.mock(DispatchAssistant.class);
        mockery.checking(new Expectations(){{
            if ( model != null ){
                allowing(dispatchAssistant).redirect(mockRequest, mockResponse, model, path); 
            } else {
                allowing(dispatchAssistant).forward(mockRequest, mockResponse, path);                     
            }
        }});     
        return dispatchAssistant;
    }

    private ViewResolver mockViewResolver(final View view, final String path) {
        final ViewResolver viewResolver = mockery.mock(ViewResolver.class);
        mockery.checking(new Expectations(){{
            allowing(viewResolver).resolve(view); will(returnValue(path));
        }});     
        return viewResolver;
    }

    private HttpServletResponse mockResponse() {       
        return mockery.mock(HttpServletResponse.class);
    }

    private HttpServletRequest mockRequest() {
        return mockery.mock(HttpServletRequest.class);
    }
    
}
