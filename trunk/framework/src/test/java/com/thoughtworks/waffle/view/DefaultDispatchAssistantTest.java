package com.thoughtworks.waffle.view;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DefaultDispatchAssistantTest extends MockObjectTestCase {

    public void testRedirectWithRequestCharacterEncodingOfNull() throws IOException {
        // Mock HttpServletRequest
        Mock mockHttpServletRequest = mock(HttpServletRequest.class);
        mockHttpServletRequest
                .expects(once())
                .method("getCharacterEncoding")
                .will(returnValue(null));
        HttpServletRequest request = (HttpServletRequest) mockHttpServletRequest.proxy();

        // Mock HttpServletResponse
        Mock mockHttpServletResponse = mock(HttpServletResponse.class);
        mockHttpServletResponse.expects(once())
                .method("setCharacterEncoding")
                .with(eq("UTF-8"));
        mockHttpServletResponse.expects(once())
                .method("encodeRedirectURL")
                .with(eq("/foobar/hello.html"))
                .will(returnValue("/foobar/hello.html;jsessionid=foobar"));
        mockHttpServletResponse.expects(once())
                .method("sendRedirect")
                .with(eq("/foobar/hello.html;jsessionid=foobar"));
        HttpServletResponse response = (HttpServletResponse) mockHttpServletResponse.proxy();

        DispatchAssistant dispatchAssistant = new DefaultDispatchAssistant();
        dispatchAssistant.redirect(request, response, new HashMap(), "/foobar/hello.html");
    }

    public void testRedirectWithRequestCharacterEncodingNotNull() throws IOException {
        // Mock HttpServletRequest
        Mock mockHttpServletRequest = mock(HttpServletRequest.class);
        mockHttpServletRequest
                .expects(once())
                .method("getCharacterEncoding")
                .will(returnValue("ISO-8859-1"));
        HttpServletRequest request = (HttpServletRequest) mockHttpServletRequest.proxy();

        // Mock HttpServletResponse
        Mock mockHttpServletResponse = mock(HttpServletResponse.class);
        mockHttpServletResponse.expects(once())
                .method("setCharacterEncoding")
                .with(eq("ISO-8859-1"));
        mockHttpServletResponse.expects(once())
                .method("encodeRedirectURL")
                .with(eq("/foobar/hello.html"))
                .will(returnValue("/foobar/hello.html;jsessionid=foobar"));
        mockHttpServletResponse.expects(once())
                .method("sendRedirect")
                .with(eq("/foobar/hello.html;jsessionid=foobar"));
        HttpServletResponse response = (HttpServletResponse) mockHttpServletResponse.proxy();

        DispatchAssistant dispatchAssistant = new DefaultDispatchAssistant();
        dispatchAssistant.redirect(request, response, new HashMap(), "/foobar/hello.html");
    }

    public void testRedirectBuildsRequestParameters() throws IOException {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("foo", 5);
        model.put("bar", "<hello world>");

        // Mock HttpServletRequest
        Mock mockHttpServletRequest = mock(HttpServletRequest.class);
        mockHttpServletRequest
                .expects(once())
                .method("getCharacterEncoding")
                .will(returnValue(null));
        HttpServletRequest request = (HttpServletRequest) mockHttpServletRequest.proxy();

        // Mock HttpServletResponse
        Mock mockHttpServletResponse = mock(HttpServletResponse.class);
        mockHttpServletResponse.expects(once())
                .method("setCharacterEncoding")
                .with(eq("UTF-8"));
        mockHttpServletResponse.expects(once())
                .method("encodeRedirectURL")
                .with(eq("/foobar/hello.html?foo=5&bar=%3Chello+world%3E"))
                .will(returnValue("/foobar/hello.html?foo=5&bar=%3Chello+world%3E;jsessionid=foobar"));
        mockHttpServletResponse.expects(once())
                .method("sendRedirect")
                .with(eq("/foobar/hello.html?foo=5&bar=%3Chello+world%3E;jsessionid=foobar"));
        HttpServletResponse response = (HttpServletResponse) mockHttpServletResponse.proxy();

        DispatchAssistant dispatchAssistant = new DefaultDispatchAssistant();
        dispatchAssistant.redirect(request, response, model, "/foobar/hello.html");
    }

    public void testRedirectAppendsRequestParameters() throws IOException {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("foo", 99);
        model.put("bar", "hola!");

        // Mock HttpServletRequest
        Mock mockHttpServletRequest = mock(HttpServletRequest.class);
        mockHttpServletRequest
                .expects(once())
                .method("getCharacterEncoding")
                .will(returnValue(null));
        HttpServletRequest request = (HttpServletRequest) mockHttpServletRequest.proxy();

        // Mock HttpServletResponse
        Mock mockHttpServletResponse = mock(HttpServletResponse.class);
        mockHttpServletResponse.expects(once())
                .method("setCharacterEncoding")
                .with(eq("UTF-8"));
        mockHttpServletResponse.expects(once())
                .method("encodeRedirectURL")
                .with(eq("/foobar/hello.html?one=two&foo=99&bar=hola%21"))
                .will(returnValue("/foobar/hello.html?one=two&foo=99&bar=hola%21;jsessionid=foobar"));
        mockHttpServletResponse.expects(once())
                .method("sendRedirect")
                .with(eq("/foobar/hello.html?one=two&foo=99&bar=hola%21;jsessionid=foobar"));
        HttpServletResponse response = (HttpServletResponse) mockHttpServletResponse.proxy();

        DispatchAssistant dispatchAssistant = new DefaultDispatchAssistant();
        dispatchAssistant.redirect(request, response, model, "/foobar/hello.html?one=two");
    }

    public void testForward() throws IOException, ServletException {
        // Mock HttpServletResponse
        Mock mockHttpServletResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockHttpServletResponse.proxy();

        // Mock RequestDispatcher
        Mock mockRequestDispatcher = mock(RequestDispatcher.class);
        mockRequestDispatcher.expects(once()).method("forward")
                .with(isA(HttpServletRequest.class), isA(HttpServletResponse.class));
        RequestDispatcher requestDispatcher = (RequestDispatcher) mockRequestDispatcher.proxy();

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once()).method("getRequestDispatcher")
                .will(returnValue(requestDispatcher));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        DispatchAssistant dispatchAssistant = new DefaultDispatchAssistant();
        dispatchAssistant.forward(request, response, "/foobar.html");
    }

}
