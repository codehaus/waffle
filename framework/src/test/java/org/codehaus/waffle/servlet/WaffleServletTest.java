/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.servlet;

import org.codehaus.waffle.Constants;
import org.codehaus.waffle.WaffleComponentRegistry;
import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;
import org.codehaus.waffle.action.ActionMethodExecutor;
import org.codehaus.waffle.action.ActionMethodResponse;
import org.codehaus.waffle.action.ActionMethodResponseHandler;
import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.DefaultActionMethodExecutor;
import org.codehaus.waffle.action.MethodInvocationException;
import org.codehaus.waffle.bind.OgnlDataBinder;
import org.codehaus.waffle.context.PicoContextContainer;
import org.codehaus.waffle.context.RequestLevelContainer;
import org.codehaus.waffle.testmodel.StubWaffleComponentRegistry;
import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.Validator;
import org.codehaus.waffle.view.View;
import ognl.DefaultTypeConverter;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.picocontainer.defaults.DefaultPicoContainer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class WaffleServletTest extends MockObjectTestCase {

    public void testInitSetsAttributeOnServletContext() throws ServletException {
        // Mock ServletConfig
        Mock mockServletConfig = mock(ServletConfig.class);
        mockServletConfig.expects(once())
                .method("getInitParameter")
                .with(eq(Constants.VIEW_PREFIX_KEY))
                .will(returnValue(null));
        mockServletConfig.expects(once())
                .method("getInitParameter")
                .with(eq(Constants.VIEW_SUFFIX_KEY))
                .will(returnValue(".jsp"));
        mockServletConfig.expects(once())
                .method("getInitParameter")
                .with(eq(Constants.METHOD_INVOCATION_ERROR_PAGE))
                .will(returnValue("foo.html"));
        final ServletConfig servletConfig = (ServletConfig) mockServletConfig.proxy();

        // Mock ServletContext
        Mock mockServletContext = mock(ServletContext.class);
        final ServletContext servletContext = (ServletContext) mockServletContext.proxy();
        mockServletContext.expects(once())
                .method("getInitParameterNames")
                .will(returnValue(null));
        mockServletContext.expects(atLeastOnce())
                .method("getInitParameter")
                .will(returnValue(null));
        mockServletContext.expects(once())
                .method("getAttribute")
                .with(eq(WaffleComponentRegistry.class.getName()))
                .will(returnValue(new StubWaffleComponentRegistry(servletContext)));

        WaffleServlet servlet = new WaffleServlet() {
            @Override
            public ServletConfig getServletConfig() {
                return servletConfig;
            }

            @Override
            public ServletContext getServletContext() {
                return servletContext;
            }
        };

        servlet.init();
    }

    public void testInitRequiresInitParameter() throws ServletException {
        Mock mockServletConext = mock(ServletContext.class);
        final ServletContext servletContext = (ServletContext) mockServletConext.proxy();
        mockServletConext.expects(once())
                .method("getAttribute")
                .with(eq("org.codehaus.waffle.WaffleComponentRegistry"))
                .will(returnValue(null));


        // Mock ServletConfig
        Mock mockServletConfig = mock(ServletConfig.class);

        mockServletConfig.expects(once())
                .method("getInitParameter")
                .with(eq(Constants.VIEW_PREFIX_KEY))
                .will(returnValue("/WEB-INF/jsp"));
        mockServletConfig.expects(once())
                .method("getInitParameter")
                .with(eq(Constants.VIEW_SUFFIX_KEY))
                .will(returnValue(null));
        mockServletConfig.expects(once())
                .method("getInitParameter")
                .with(eq(Constants.METHOD_INVOCATION_ERROR_PAGE))
                .will(returnValue("foo.html"));        
        final ServletConfig servletConfig = (ServletConfig) mockServletConfig.proxy();

        WaffleServlet servlet = new WaffleServlet() {
            @Override
            public ServletConfig getServletConfig() {
                return servletConfig;
            }

            @Override
            public ServletContext getServletContext() {
                return servletContext;
            }
        };

        try {
            servlet.init();
            fail("WaffleException was expected");
        } catch (WaffleException expected) {
            // expected
        }
    }

    public void testServiceForNonDispatchingController() throws Exception {
        RequestLevelContainer.set(new PicoContextContainer(new DefaultPicoContainer()));
        final NonDispatchingController nonDispatchingController = new NonDispatchingController();
        List<?> list = Collections.EMPTY_LIST;
        Enumeration enumeration = Collections.enumeration(list);

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getParameterNames")
                .will(returnValue(enumeration));
        mockRequest.expects(once())
                .method("setAttribute")
                .with(eq(Constants.ERRORS_KEY), isA(ErrorsContext.class));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        Method method = NonDispatchingController.class.getMethod("increment");
        final MethodDefinition methodDefinition = new MethodDefinition(method);

        // stub out what we don't want called ... execute it
        WaffleServlet waffleServlet = new WaffleServlet() {
            @Override
            protected ControllerDefinition getControllerDefinition(HttpServletRequest request, HttpServletResponse response) {
                return new ControllerDefinition("no name", nonDispatchingController, methodDefinition);
            }
        };

        // Mock ActionMethodResponseHandler
        Mock mockMethodResponseHandler = mock(ActionMethodResponseHandler.class);
        mockMethodResponseHandler.expects(once())
                .method("handle")
                .with(eq(request), ANYTHING, isA(ActionMethodResponse.class));
        ActionMethodResponseHandler actionMethodResponseHandler = (ActionMethodResponseHandler) mockMethodResponseHandler.proxy();

        // Mock Validator
        Mock mockValidator = mock(Validator.class);
        mockValidator.expects(once())
                .method("validate")
                .with(isA(ControllerDefinition.class), isA(ErrorsContext.class));
        Validator validator = (Validator) mockValidator.proxy();

        // Set up what normally would happen via "init()"
        Field dataBinderField = WaffleServlet.class.getDeclaredField("dataBinder");
        dataBinderField.setAccessible(true);
        dataBinderField.set(waffleServlet, new OgnlDataBinder(new DefaultTypeConverter(), null));
        Field actionMethodExecutorField = WaffleServlet.class.getDeclaredField("actionMethodExecutor");
        actionMethodExecutorField.setAccessible(true);
        actionMethodExecutorField.set(waffleServlet, new DefaultActionMethodExecutor());
        Field methodResponseHandlerField = WaffleServlet.class.getDeclaredField("actionMethodResponseHandler");
        methodResponseHandlerField.setAccessible(true);
        methodResponseHandlerField.set(waffleServlet, actionMethodResponseHandler);
        Field validatorFactoryField = WaffleServlet.class.getDeclaredField("validator");
        validatorFactoryField.setAccessible(true);
        validatorFactoryField.set(waffleServlet, validator);

        waffleServlet.service(request, response);
        assertEquals(1, nonDispatchingController.getCount());
    }

    public void testControllerNotFound() throws Exception {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("setAttribute")
                .with(eq(Constants.ERRORS_KEY), isA(ErrorsContext.class));
        mockRequest.expects(once())
                .method("getServletPath")
                .will(returnValue("/foobar"));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock ControllerDefinitionFactory
        Mock mockFactory = mock(ControllerDefinitionFactory.class);
        mockFactory.expects(once())
                .method("getControllerDefinition")
                .will(returnValue(new ControllerDefinition("junk", null, null)));
        ControllerDefinitionFactory controllerDefinitionFactory = (ControllerDefinitionFactory) mockFactory.proxy();

        WaffleServlet waffleServlet = new WaffleServlet();

        // Set up what normally would happen via "init()"
        Field actionFactoryField = WaffleServlet.class.getDeclaredField("controllerDefinitionFactory");
        actionFactoryField.setAccessible(true);
        actionFactoryField.set(waffleServlet, controllerDefinitionFactory);

        try {
            waffleServlet.service(request, null);
            fail("ServletException expected when an invalid controller is requested");
        } catch (ServletException expected) {
            assertTrue(expected.getMessage().startsWith("Unable to locate the Waffle-Controller"));
        }
    }

    public void testMethodDefinitionIsNull() throws Exception {
        final NonDispatchingController nonDispatchingController = new NonDispatchingController();
        List<?> list = Collections.EMPTY_LIST;
        Enumeration enumeration = Collections.enumeration(list);

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once()).method("getParameterNames").will(returnValue(enumeration));
        mockRequest.expects(once())
                .method("setAttribute")
                .with(eq(Constants.ERRORS_KEY), isA(ErrorsContext.class));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // stub out what we don't want called ... execute it
        WaffleServlet waffleServlet = new WaffleServlet() {
            @Override
            protected ControllerDefinition getControllerDefinition(HttpServletRequest request, HttpServletResponse response) {
                return new ControllerDefinition("no name", nonDispatchingController, null);
            }

            @Override
            public void log(String string) {
                // ignore
            }
        };

        // Mock ActionMethodResponseHandler
        Mock mockActionMethodResponseHandler = mock(ActionMethodResponseHandler.class);
        mockActionMethodResponseHandler.expects(once())
                .method("handle")
                .with(eq(request), ANYTHING, isA(ActionMethodResponse.class));
        ActionMethodResponseHandler actionMethodResponseHandler =
                (ActionMethodResponseHandler) mockActionMethodResponseHandler.proxy();

        // Mock Validator
        Mock mockValidator = mock(Validator.class);
        mockValidator.expects(once())
                .method("validate")
                .with(isA(ControllerDefinition.class), isA(ErrorsContext.class));
        Validator validator = (Validator) mockValidator.proxy();

        // Set up what normally would happen via "init()"
        Field dataBinderField = WaffleServlet.class.getDeclaredField("dataBinder");
        dataBinderField.setAccessible(true);
        dataBinderField.set(waffleServlet, new OgnlDataBinder(new DefaultTypeConverter(), null));

        Field methodResponseHandlerField = WaffleServlet.class.getDeclaredField("actionMethodResponseHandler");
        methodResponseHandlerField.setAccessible(true);
        methodResponseHandlerField.set(waffleServlet, actionMethodResponseHandler);

        Field validatorFactoryField = WaffleServlet.class.getDeclaredField("validator");
        validatorFactoryField.setAccessible(true);
        validatorFactoryField.set(waffleServlet, validator);

        waffleServlet.service(request, response);
    }

    public void testMethodInvocationExceptionThrown() throws Exception {
        final NonDispatchingController nonDispatchingController = new NonDispatchingController();
        List<?> list = Collections.EMPTY_LIST;
        Enumeration enumeration = Collections.enumeration(list);

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once()).method("getParameterNames").will(returnValue(enumeration));
        mockRequest.expects(once())
                .method("setAttribute")
                .with(eq(Constants.ERRORS_KEY), isA(ErrorsContext.class));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        mockResponse.expects(once())
                .method("sendError")
                .with(eq(HttpServletResponse.SC_INTERNAL_SERVER_ERROR), isA(String.class));
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // stub out what we don't want called ... execute it
        WaffleServlet waffleServlet = new WaffleServlet() {
            @Override
            protected ControllerDefinition getControllerDefinition(HttpServletRequest request, HttpServletResponse response) {
                return new ControllerDefinition("no name", nonDispatchingController, new MethodDefinition(null));
            }

            @Override
            public void log(String string) {
                // ignore
            }
        };

        // Mock ActionMethodExecutor
        Mock mockMethodExecutor = mock(ActionMethodExecutor.class);
        mockMethodExecutor.expects(once())
                .method("execute")
                .with(isA(ActionMethodResponse.class), isA(ControllerDefinition.class))
                .will(throwException(new MethodInvocationException("fake from test")));
        ActionMethodExecutor actionMethodExecutor = (ActionMethodExecutor) mockMethodExecutor.proxy();

        // Mock Validator
        Mock mockValidator = mock(Validator.class);
        mockValidator.expects(once())
                .method("validate")
                .with(isA(ControllerDefinition.class), isA(ErrorsContext.class));
        Validator validator = (Validator) mockValidator.proxy();

        // Set up what normally would happen via "init()"
        Field dataBinderField = WaffleServlet.class.getDeclaredField("dataBinder");
        dataBinderField.setAccessible(true);
        dataBinderField.set(waffleServlet, new OgnlDataBinder(new DefaultTypeConverter(), null));

        Field mockMethodExecutorField = WaffleServlet.class.getDeclaredField("actionMethodExecutor");
        mockMethodExecutorField.setAccessible(true);
        mockMethodExecutorField.set(waffleServlet, actionMethodExecutor);

        Field validatorFactoryField = WaffleServlet.class.getDeclaredField("validator");
        validatorFactoryField.setAccessible(true);
        validatorFactoryField.set(waffleServlet, validator);

        waffleServlet.service(request, response);
    }

    public void testBuildViewToReferrer() throws Exception {
        WaffleServlet waffleServlet = new WaffleServlet();

        Field viewPrefixField = WaffleServlet.class.getDeclaredField("viewPrefix");
        viewPrefixField.setAccessible(true);
        viewPrefixField.set(waffleServlet, "prefix-");

        Field viewSuffixField = WaffleServlet.class.getDeclaredField("viewSuffix");
        viewSuffixField.setAccessible(true);
        viewSuffixField.set(waffleServlet, "-suffix");

        ControllerDefinition controllerDefinition = new ControllerDefinition("foobar", null, null);
        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        waffleServlet.buildViewToReferrer(controllerDefinition, actionMethodResponse);

        View view = (View) actionMethodResponse.getReturnValue();
        assertEquals("prefix-foobar-suffix", view.getValue());
    }

    public class NonDispatchingController {
        private int count = 0;

        public void increment() {
            count += 1;
        }

        public int getCount() {
            return count;
        }
    }

    public class StubServletOutputStream extends ServletOutputStream {
        public StringBuffer buffer = new StringBuffer();

        public void print(String string) throws IOException {
            buffer.append(string);
        }

        public void write(int b) throws IOException {

        }
    }

}
