/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.servlet;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.Constants;
import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.action.ActionMethodExecutor;
import org.codehaus.waffle.action.ActionMethodInvocationException;
import org.codehaus.waffle.action.ActionMethodResponse;
import org.codehaus.waffle.action.ActionMethodResponseHandler;
import org.codehaus.waffle.action.InterceptingActionMethodExecutor;
import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.intercept.MethodInterceptor;
import org.codehaus.waffle.bind.RequestAttributeBinder;
import org.codehaus.waffle.bind.ognl.OgnlDataBinder;
import org.codehaus.waffle.bind.ognl.OgnlValueConverterFinder;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.RequestLevelContainer;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;
import org.codehaus.waffle.i18n.DefaultMessagesContext;
import org.codehaus.waffle.i18n.MessagesContext;
import org.codehaus.waffle.monitor.ServletMonitor;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.Validator;
import org.codehaus.waffle.view.View;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class WaffleServletTest {
    private final Mockery mockery = new Mockery();

    @SuppressWarnings("serial")
    @Test
    public void canConfigureComponentsViaInitAttributes() throws ServletException {
        // Mock ServletConfig
        final ServletConfig servletConfig = mockery.mock(ServletConfig.class);
        mockery.checking(new Expectations() {{
            one(servletConfig).getInitParameter(Constants.VIEW_PREFIX_KEY);
            will(returnValue(null));
            one(servletConfig).getInitParameter(Constants.VIEW_SUFFIX_KEY);
            will(returnValue(".jsp"));
        }});

        // Mock ComponentRegistry        
        final ComponentRegistry componentRegistry = mockery.mock(ComponentRegistry.class);
        mockery.checking(new Expectations() {{
            one(componentRegistry).getActionMethodExecutor();
            one(componentRegistry).getActionMethodResponseHandler();
            one(componentRegistry).getServletMonitor();
            one(componentRegistry).getDataBinder();
            one(componentRegistry).getRequestAttributeBinder();
            one(componentRegistry).getControllerDefinitionFactory();
            one(componentRegistry).getValidator();
        }});

        // Mock ServletContext        
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {{
            one(servletContext).getAttribute(ComponentRegistry.class.getName());
            will(returnValue(componentRegistry));
        }});


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

    @SuppressWarnings("serial")
    @Test
    public void canServiceNonDispatchingController() throws Exception {
        // Mock ErrorsContext
        final ErrorsContext errorsContext = mockery.mock(ErrorsContext.class);
        final ContextContainer contextContainer = mockery.mock(ContextContainer.class);
        mockery.checking(new Expectations() {
            {
                
                one(contextContainer).getComponentInstanceOfType(ErrorsContext.class);
                will(returnValue(errorsContext));
                exactly(2).of(errorsContext).hasErrorMessages();
                will(returnValue(false));
                one(contextContainer).getAllComponentInstancesOfType(MethodInterceptor.class);
                will(returnValue(new ArrayList<Object>()));
            }
        });

        RequestLevelContainer.set(contextContainer);
        final NonDispatchingController nonDispatchingController = new NonDispatchingController();
        List<?> list = Collections.EMPTY_LIST;
        final Enumeration<?> enumeration = Collections.enumeration(list);

        // Mock ServletConfig
        final ServletConfig servletConfig = mockery.mock(ServletConfig.class);

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {{
            atLeast(1).of(request).getParameterNames();
            will(returnValue(enumeration));
            one(request).getMethod();
            will(returnValue("get"));
        }});

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);
        
        Method method = NonDispatchingController.class.getMethod("increment");
        final MethodDefinition methodDefinition = new MethodDefinition(method);

        // Mock ActionMethodResponseHandler
        final ActionMethodResponseHandler actionMethodResponseHandler = mockery.mock(ActionMethodResponseHandler.class);
        mockery.checking(new Expectations() {{
            one(actionMethodResponseHandler).handle(with(same(request)), with(same(response)), with(any(ActionMethodResponse.class)));
        }});

        // Mock Validator
        final Validator validator = mockery.mock(Validator.class);
        mockery.checking(new Expectations() {{
            one(validator).validate(with(any(ControllerDefinition.class)), with(any(ErrorsContext.class)));
        }});

        // Mock RequestAttributeBinder
        final RequestAttributeBinder requestAttributeBinder = mockery.mock(RequestAttributeBinder.class);
        mockery.checking(new Expectations() {{
            one(requestAttributeBinder).bind(with(same(request)), with(any(NonDispatchingController.class)));
        }});

        // stub out what we don't want called ... execute it
        SilentMonitor monitor = new SilentMonitor();
        WaffleServlet waffleServlet = new WaffleServlet(new InterceptingActionMethodExecutor(monitor),
                                                        actionMethodResponseHandler,
                                                        monitor,
                                                        new OgnlDataBinder(new OgnlValueConverterFinder(), null, monitor),
                                                        requestAttributeBinder,
                                                        null, validator) {
            @Override
            protected ControllerDefinition getControllerDefinition(HttpServletRequest request, HttpServletResponse response) {
                return new ControllerDefinition("no name", nonDispatchingController, methodDefinition);
            }
            @Override
            public ServletConfig getServletConfig() {
                return servletConfig;
            }
        };

        waffleServlet.service(request, response);
        Assert.assertEquals(1, nonDispatchingController.getCount());
    }

    @SuppressWarnings("serial")
    @Test // Testing Post/Redirect/Get - see http://en.wikipedia.org/wiki/Post/Redirect/Get
    public void serviceShouldCreateRedirectViewWhenReturnValueIsNullAndRequestWasAPost() throws Exception {
        // Mock ErrorsContext
        final ErrorsContext errorsContext = mockery.mock(ErrorsContext.class);
        final ContextContainer contextContainer = mockery.mock(ContextContainer.class);
        mockery.checking(new Expectations() {
            {

                one(contextContainer).getComponentInstanceOfType(ErrorsContext.class);
                will(returnValue(errorsContext));
                exactly(2).of(errorsContext).hasErrorMessages();
                will(returnValue(false));
                one(contextContainer).getAllComponentInstancesOfType(MethodInterceptor.class);
                will(returnValue(new ArrayList<Object>()));
            }
        });

        RequestLevelContainer.set(contextContainer);
        final NonDispatchingController nonDispatchingController = new NonDispatchingController();
        List<?> list = Collections.EMPTY_LIST;
        final Enumeration<?> enumeration = Collections.enumeration(list);

        // Mock ServletConfig
        final ServletConfig servletConfig = mockery.mock(ServletConfig.class);

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {{
            atLeast(1).of(request).getParameterNames();
            will(returnValue(enumeration));
            one(request).getMethod();
            will(returnValue("post"));
            one(request).getRequestURL();
            will(returnValue(new StringBuffer("www.chicagobears.com")));
        }});

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        Method method = NonDispatchingController.class.getMethod("increment");
        final MethodDefinition methodDefinition = new MethodDefinition(method);

        // Mock ActionMethodResponseHandler
        final ActionMethodResponseHandler actionMethodResponseHandler = mockery.mock(ActionMethodResponseHandler.class);
        mockery.checking(new Expectations() {{
            one(actionMethodResponseHandler).handle(with(same(request)), with(same(response)), with(any(ActionMethodResponse.class)));
        }});

        // Mock Validator
        final Validator validator = mockery.mock(Validator.class);
        mockery.checking(new Expectations() {{
            one(validator).validate(with(any(ControllerDefinition.class)), with(any(ErrorsContext.class)));
        }});

        // Mock RequestAttributeBinder
        final RequestAttributeBinder requestAttributeBinder = mockery.mock(RequestAttributeBinder.class);
        mockery.checking(new Expectations() {{
            one(requestAttributeBinder).bind(with(same(request)), with(any(NonDispatchingController.class)));
        }});

        // stub out what we don't want called ... execute it
        SilentMonitor monitor = new SilentMonitor();
        WaffleServlet waffleServlet = new WaffleServlet(new InterceptingActionMethodExecutor(monitor),
                                                        actionMethodResponseHandler,
                                                        monitor,
                                                        new OgnlDataBinder(new OgnlValueConverterFinder(), null, monitor),
                                                        requestAttributeBinder,
                                                        null, validator) {
            @Override
            protected ControllerDefinition getControllerDefinition(HttpServletRequest request, HttpServletResponse response) {
                return new ControllerDefinition("no name", nonDispatchingController, methodDefinition);
            }
            @Override
            public ServletConfig getServletConfig() {
                return servletConfig;
            }
        };

        waffleServlet.service(request, response);
        Assert.assertEquals(1, nonDispatchingController.getCount());
    }

    @SuppressWarnings({ "serial", "unchecked" })
    @Test(expected = ServletException.class)
    public void cannotServiceIfControllerNotFound() throws Exception {
        // Mock ErrorsContext
        final ErrorsContext errorsContext = mockery.mock(ErrorsContext.class);
        final ContextContainer contextContainer = mockery.mock(ContextContainer.class);
        mockery.checking(new Expectations() {
            {

                one(contextContainer).getComponentInstanceOfType(ErrorsContext.class);
                will(returnValue(errorsContext));
                one(errorsContext).hasErrorMessages();
                will(returnValue(false));
                one(contextContainer).getAllComponentInstancesOfType(MethodInterceptor.class);
                will(returnValue(new ArrayList<Object>()));
            }
        });

        RequestLevelContainer.set(contextContainer);
        
        // Mock ServletConfig
        final ServletConfig servletConfig = mockery.mock(ServletConfig.class);

        List<?> list = Collections.EMPTY_LIST;
        final Enumeration<?> enumeration = Collections.enumeration(list);

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {{
            atLeast(1).of(request).getParameterNames();
            will(returnValue(enumeration));
            one(request).getServletPath();
            will(returnValue("/foobar"));
        }});

        // Mock ControllerDefinitionFactory
        final ControllerDefinitionFactory controllerDefinitionFactory = mockery.mock(ControllerDefinitionFactory.class);
        mockery.checking(new Expectations() {{
            one(controllerDefinitionFactory).getControllerDefinition(request, null);
            will(returnValue(new ControllerDefinition("junk", null, null)));
        }});

        WaffleServlet waffleServlet = new WaffleServlet(){
            @Override
            public ServletConfig getServletConfig() {
                return servletConfig;
            }
        };

        // Mock ServletMonitor
        final ServletMonitor servletMonitor = mockery.mock(ServletMonitor.class);
        mockery.checking(new Expectations() {{
            allowing(servletMonitor).servletServiceRequested(with(any(Map.class)));
        }});
        
        // Set up what normally would happen via "init()"
        Field actionFactoryField = WaffleServlet.class.getDeclaredField("controllerDefinitionFactory");
        actionFactoryField.setAccessible(true);
        actionFactoryField.set(waffleServlet, controllerDefinitionFactory);

        Field servletMonitorField = WaffleServlet.class.getDeclaredField("servletMonitor");
        servletMonitorField.setAccessible(true);
        servletMonitorField.set(waffleServlet, servletMonitor);

        waffleServlet.service(request, null);
    }

    @SuppressWarnings("serial")
    @Test
    public void cannotServiceIfMethodDefinitionIsNull() throws Exception {
        // Mock ErrorsContext
        final ErrorsContext errorsContext = mockery.mock(ErrorsContext.class);
        final ContextContainer contextContainer = mockery.mock(ContextContainer.class);
        mockery.checking(new Expectations() {
            {
                one(contextContainer).getComponentInstanceOfType(ErrorsContext.class);
                will(returnValue(errorsContext));
                one(errorsContext).hasErrorMessages();
                will(returnValue(false));
            }
        });

        RequestLevelContainer.set(contextContainer);
        final NonDispatchingController nonDispatchingController = new NonDispatchingController();
        List<?> list = Collections.EMPTY_LIST;
        final Enumeration<?> enumeration = Collections.enumeration(list);
        
        // Mock ServletConfig
        final ServletConfig servletConfig = mockery.mock(ServletConfig.class);

        // Mock HttpServletRequest
        final HttpServletRequest request  = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {{
            atLeast(1).of(request).getParameterNames();
            will(returnValue(enumeration));
        }});

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock ActionMethodResponseHandler
        final ActionMethodResponseHandler actionMethodResponseHandler = mockery.mock(ActionMethodResponseHandler.class);
        mockery.checking(new Expectations() {{
            one(actionMethodResponseHandler).handle(with(same(request)), with(same(response)), with(any(ActionMethodResponse.class)));
        }});

        // Mock Validator
        final Validator validator = mockery.mock(Validator.class);
        mockery.checking(new Expectations() {{
            one(validator).validate(with(any(ControllerDefinition.class)), with(any(ErrorsContext.class)));
        }});

        // Mock
        final RequestAttributeBinder requestAttributeBinder = mockery.mock(RequestAttributeBinder.class);
        mockery.checking(new Expectations() {{
            one(requestAttributeBinder).bind(with(same(request)), with(any(NonDispatchingController.class)));
        }});

        // stub out what we don't want called ... execute it
        WaffleServlet waffleServlet = new WaffleServlet(null,
                actionMethodResponseHandler,
                new SilentMonitor(),
                new OgnlDataBinder(new OgnlValueConverterFinder(), null, new SilentMonitor()),
                requestAttributeBinder,
                null, validator) {
            @Override
            protected ControllerDefinition getControllerDefinition(HttpServletRequest request, HttpServletResponse response) {
                return new ControllerDefinition("no name", nonDispatchingController, null);
            }
            @Override
            public ServletConfig getServletConfig() {
                return servletConfig;
            }            
            @Override
            public void log(String string) {
                // ignore
            }
        };

        waffleServlet.service(request, response);
    }

    @SuppressWarnings({"serial", "ThrowableInstanceNeverThrown", "unchecked"})
    @Test
    public void canThrowExceptionInMethodInvocation() throws Exception {
        // Mock ErrorsContext
        final ErrorsContext errorsContext = mockery.mock(ErrorsContext.class);
        final ContextContainer contextContainer = mockery.mock(ContextContainer.class);
        mockery.checking(new Expectations() {
            {
                one(contextContainer).getComponentInstanceOfType(ErrorsContext.class);
                will(returnValue(errorsContext));
                one(errorsContext).hasErrorMessages();
                will(returnValue(false));
            }
        });

        RequestLevelContainer.set(contextContainer);

        final NonDispatchingController nonDispatchingController = new NonDispatchingController();
        List<?> list = Collections.EMPTY_LIST;
        final Enumeration<?> enumeration = Collections.enumeration(list);

        // Mock ServletContext
        final ServletConfig servletConfig = mockery.mock(ServletConfig.class);

        // Mock HttpServletRequest
        final HttpServletRequest request  = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {{
            atLeast(1).of(request).getParameterNames();
            will(returnValue(enumeration));
        }});

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);
        mockery.checking(new Expectations() {{
            one(response).sendError(with(equal(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)), with(any(String.class)));
        }});

        // stub out what we don't want called ... execute it
        WaffleServlet waffleServlet = new WaffleServlet() {
            @Override
            protected ControllerDefinition getControllerDefinition(HttpServletRequest request, HttpServletResponse response) {
                return new ControllerDefinition("no name", nonDispatchingController, new MethodDefinition(null));
            }            
            @Override
            public ServletConfig getServletConfig() {
                return servletConfig;
            }
            @Override
            public void log(String string) {
                // ignore
            }
        };

        // Mock ActionMethodExecutor
        final ActionMethodExecutor actionMethodExecutor = mockery.mock(ActionMethodExecutor.class);
        final ActionMethodInvocationException actionMethodInvocationException = new ActionMethodInvocationException("fake from test");
        mockery.checking(new Expectations() {{
            one(actionMethodExecutor).execute(with(any(ActionMethodResponse.class)), with(any(ControllerDefinition.class)));
            will(throwException(actionMethodInvocationException));
        }});

        // Mock ServletMonitor
        final ServletMonitor servletMonitor = mockery.mock(ServletMonitor.class);
        mockery.checking(new Expectations() {{
            allowing(servletMonitor).servletServiceRequested(with(any(Map.class)));
            allowing(servletMonitor).servletServiceFailed(actionMethodInvocationException);
        }});

        // Mock Validator
        final Validator validator = mockery.mock(Validator.class);
        mockery.checking(new Expectations() {{
            one(validator).validate(with(any(ControllerDefinition.class)), with(any(ErrorsContext.class)));
        }});

        // Set up what normally would happen via "init()"
        Field dataBinderField = WaffleServlet.class.getDeclaredField("dataBinder");
        dataBinderField.setAccessible(true);
        dataBinderField.set(waffleServlet, new OgnlDataBinder(new OgnlValueConverterFinder(), null, new SilentMonitor()));

        Field actionMethodExecutorField = WaffleServlet.class.getDeclaredField("actionMethodExecutor");
        actionMethodExecutorField.setAccessible(true);
        actionMethodExecutorField.set(waffleServlet, actionMethodExecutor);

        Field servletMonitorField = WaffleServlet.class.getDeclaredField("servletMonitor");
        servletMonitorField.setAccessible(true);
        servletMonitorField.set(waffleServlet, servletMonitor);

        Field validatorFactoryField = WaffleServlet.class.getDeclaredField("validator");
        validatorFactoryField.setAccessible(true);
        validatorFactoryField.set(waffleServlet, validator);

        waffleServlet.service(request, response);
    }

    @Test
    public void canBuildViewToReferrer() throws Exception {
        WaffleServlet waffleServlet = new WaffleServlet();

        Field viewPrefixField = WaffleServlet.class.getDeclaredField("viewPrefix");
        viewPrefixField.setAccessible(true);
        viewPrefixField.set(waffleServlet, "prefix-");

        Field viewSuffixField = WaffleServlet.class.getDeclaredField("viewSuffix");
        viewSuffixField.setAccessible(true);
        viewSuffixField.set(waffleServlet, "-suffix");

        ControllerDefinition controllerDefinition = new ControllerDefinition("foobar", null, null);
        View view = waffleServlet.buildReferringView(controllerDefinition);

        Assert.assertEquals("prefix-foobar-suffix", view.getValue());
    }

    @SuppressWarnings("serial")
    @Test(expected = WaffleException.class)
    public void cannotInitWithoutParameter() throws ServletException {
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {{
            one(servletContext).getAttribute(ComponentRegistry.class.getName());
            will(returnValue(null));
        }});

        // Mock ServletConfig
        final ServletConfig servletConfig = mockery.mock(ServletConfig.class);
        mockery.checking(new Expectations() {{
            one(servletConfig).getInitParameter(Constants.VIEW_PREFIX_KEY);
            will(returnValue("/WEB-INF/jsp"));
            one(servletConfig).getInitParameter(Constants.VIEW_SUFFIX_KEY);
        }});

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

    public class NonDispatchingController {
        private int count = 0;
        private MessagesContext messages = new DefaultMessagesContext(null);
        
        public void increment() {
            count += 1;
            messages.addMessage("success", "Incremented count");
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
