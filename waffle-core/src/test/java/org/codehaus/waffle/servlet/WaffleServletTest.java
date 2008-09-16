/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.servlet;

import static org.codehaus.waffle.Constants.ERRORS_VIEW_KEY;
import static org.codehaus.waffle.Constants.VIEW_PREFIX_KEY;
import static org.codehaus.waffle.Constants.VIEW_SUFFIX_KEY;
import static org.junit.Assert.assertEquals;

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
import org.codehaus.waffle.bind.ViewDataBinder;
import org.codehaus.waffle.bind.ognl.OgnlControllerDataBinder;
import org.codehaus.waffle.bind.ognl.OgnlValueConverterFinder;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.RequestLevelContainer;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;
import org.codehaus.waffle.controller.ControllerNotFoundException;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.i18n.DefaultMessagesContext;
import org.codehaus.waffle.i18n.MessagesContext;
import org.codehaus.waffle.monitor.ServletMonitor;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.validation.ErrorMessage;
import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.Validator;
import org.codehaus.waffle.view.DefaultViewResolver;
import org.codehaus.waffle.view.View;
import org.codehaus.waffle.view.ViewResolver;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
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
        mockery.checking(new Expectations() {
            {
                one(servletConfig).getInitParameter(VIEW_PREFIX_KEY);
                will(returnValue(null));
                one(servletConfig).getInitParameter(VIEW_SUFFIX_KEY);
                will(returnValue(".jsp"));
                one(servletConfig).getInitParameter(ERRORS_VIEW_KEY);
                will(returnValue("errors"));
            }
        });

        // Mock ComponentRegistry
        final ComponentRegistry componentRegistry = mockery.mock(ComponentRegistry.class);
        mockery.checking(new Expectations() {
            {
                one(componentRegistry).getActionMethodExecutor();
                one(componentRegistry).getActionMethodResponseHandler();
                one(componentRegistry).getServletMonitor();
                one(componentRegistry).getControllerDataBinder();
                one(componentRegistry).getControllerDefinitionFactory();
                one(componentRegistry).getMessageResources();
                one(componentRegistry).getViewDataBinder();
                one(componentRegistry).getViewResolver();
                one(componentRegistry).getValidator();
            }
        });

        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {
            {
                one(servletContext).getAttribute(ComponentRegistry.class.getName());
                will(returnValue(componentRegistry));
            }
        });

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

                one(contextContainer).getComponent(ErrorsContext.class);
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
        mockery.checking(new Expectations() {
            {
                atLeast(1).of(request).getParameterNames();
                will(returnValue(enumeration));
                one(request).getMethod();
                will(returnValue("get"));
            }
        });

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        Method method = NonDispatchingController.class.getMethod("increment");
        final MethodDefinition methodDefinition = new MethodDefinition(method);

        // Mock ActionMethodResponseHandler
        final ActionMethodResponseHandler actionMethodResponseHandler = mockery.mock(ActionMethodResponseHandler.class);
        mockery.checking(new Expectations() {
            {
                one(actionMethodResponseHandler).handle(with(same(request)), with(same(response)),
                        with(any(ActionMethodResponse.class)));
            }
        });

        // Mock Validator
        final Validator validator = mockery.mock(Validator.class);
        mockery.checking(new Expectations() {
            {
                one(validator).validate(with(any(ControllerDefinition.class)), with(any(ErrorsContext.class)));
            }
        });

        // Mock ViewDataBinder
        final ViewDataBinder viewDataBinder = mockery.mock(ViewDataBinder.class);
        mockery.checking(new Expectations() {
            {
                one(viewDataBinder).bind(with(same(request)), with(any(NonDispatchingController.class)));
            }
        });

        // Mock ControllerDefinitionFactory
        final ControllerDefinitionFactory controllerDefinitionFactory = mockery.mock(ControllerDefinitionFactory.class);
        mockery.checking(new Expectations() {
            {
                one(controllerDefinitionFactory).getControllerDefinition(with(same(request)), with(same(response)));
                will(returnValue(new ControllerDefinition("no name", nonDispatchingController, methodDefinition)));
            }
        });

        // stub out what we don't want called ... execute it
        SilentMonitor monitor = new SilentMonitor();
        WaffleServlet servlet = new WaffleServlet(new InterceptingActionMethodExecutor(monitor),
                actionMethodResponseHandler, monitor, new OgnlControllerDataBinder(new OgnlValueConverterFinder(),
                        null, monitor), controllerDefinitionFactory, new DefaultMessageResources(), viewDataBinder,
                new DefaultViewResolver(), validator) {
            @Override
            public ServletConfig getServletConfig() {
                return servletConfig;
            }
        };

        servlet.service(request, response);
        assertEquals(1, nonDispatchingController.getCount());
    }

    @SuppressWarnings("serial")
    @Test
    // Testing Post/Redirect/Get - see http://en.wikipedia.org/wiki/Post/Redirect/Get
    public void canCreateRedirectViewWhenReturnValueIsNullAndRequestWasAPost() throws Exception {
        // Mock ErrorsContext
        final ErrorsContext errorsContext = mockery.mock(ErrorsContext.class);
        final ContextContainer contextContainer = mockery.mock(ContextContainer.class);
        mockery.checking(new Expectations() {
            {

                one(contextContainer).getComponent(ErrorsContext.class);
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
        mockery.checking(new Expectations() {
            {
                atLeast(1).of(request).getParameterNames();
                will(returnValue(enumeration));
                one(request).getMethod();
                will(returnValue("post"));
                one(request).getRequestURL();
                will(returnValue(new StringBuffer("www.chicagobears.com")));
            }
        });

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        Method method = NonDispatchingController.class.getMethod("increment");
        final MethodDefinition methodDefinition = new MethodDefinition(method);

        // Mock ActionMethodResponseHandler
        final ActionMethodResponseHandler actionMethodResponseHandler = mockery.mock(ActionMethodResponseHandler.class);
        mockery.checking(new Expectations() {
            {
                one(actionMethodResponseHandler).handle(with(same(request)), with(same(response)),
                        with(any(ActionMethodResponse.class)));
            }
        });

        // Mock Validator
        final Validator validator = mockery.mock(Validator.class);
        mockery.checking(new Expectations() {
            {
                one(validator).validate(with(any(ControllerDefinition.class)), with(any(ErrorsContext.class)));
            }
        });

        // Mock ViewDataBinder
        final ViewDataBinder viewDataBinder = mockery.mock(ViewDataBinder.class);
        mockery.checking(new Expectations() {
            {
                one(viewDataBinder).bind(with(same(request)), with(any(NonDispatchingController.class)));
            }
        });

        // Mock ControllerDefinitionFactory
        final ControllerDefinitionFactory controllerDefinitionFactory = mockery.mock(ControllerDefinitionFactory.class);
        mockery.checking(new Expectations() {
            {
                one(controllerDefinitionFactory).getControllerDefinition(with(same(request)), with(same(response)));
                will(returnValue(new ControllerDefinition("no name", nonDispatchingController, methodDefinition)));
            }
        });

        // stub out what we don't want called ... execute it
        SilentMonitor monitor = new SilentMonitor();
        WaffleServlet servlet = new WaffleServlet(new InterceptingActionMethodExecutor(monitor),
                actionMethodResponseHandler, monitor, new OgnlControllerDataBinder(new OgnlValueConverterFinder(),
                        null, monitor), controllerDefinitionFactory, new DefaultMessageResources(), viewDataBinder,
                new DefaultViewResolver(), validator) {
            @Override
            public ServletConfig getServletConfig() {
                return servletConfig;
            }
        };

        servlet.service(request, response);
        assertEquals(1, nonDispatchingController.getCount());
    }

    @SuppressWarnings( { "serial", "unchecked" })
    @Test
    public void canHandleSystemExceptions() throws Exception {
        // Mock ErrorsContext
        final ErrorsContext errorsContext = mockery.mock(ErrorsContext.class);
        final ContextContainer contextContainer = mockery.mock(ContextContainer.class);
        mockery.checking(new Expectations() {
            {
                one(contextContainer).getComponent(ErrorsContext.class);
                will(returnValue(errorsContext));
                one(errorsContext).addErrorMessage(with(any(ErrorMessage.class)));
            }
        });

        RequestLevelContainer.set(contextContainer);

        // Mock ServletConfig
        final ServletConfig servletConfig = mockery.mock(ServletConfig.class);

        List<?> list = Collections.EMPTY_LIST;
        final Enumeration<?> enumeration = Collections.enumeration(list);

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                atLeast(1).of(request).getParameterNames();
                will(returnValue(enumeration));
            }
        });

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock ActionMethodResponseHandler
        final ActionMethodResponseHandler actionMethodResponseHandler = mockery.mock(ActionMethodResponseHandler.class);
        mockery.checking(new Expectations() {
            {
                one(actionMethodResponseHandler).handle(with(same(request)), with(same(response)),
                        with(any(ActionMethodResponse.class)));
            }
        });

        // Mock ControllerDefinitionFactory
        final ControllerDefinitionFactory controllerDefinitionFactory = mockery.mock(ControllerDefinitionFactory.class);
        mockery.checking(new Expectations() {
            {
                one(controllerDefinitionFactory).getControllerDefinition(request, response);
                will(throwException(new ControllerNotFoundException("No controller found ")));
            }
        });

        // stub out what we don't want called ... execute it
        WaffleServlet servlet = new WaffleServlet(null, actionMethodResponseHandler, new SilentMonitor(),
                new OgnlControllerDataBinder(new OgnlValueConverterFinder(), null, new SilentMonitor()),
                controllerDefinitionFactory, new DefaultMessageResources(), null, new DefaultViewResolver(), null) {
            @Override
            public ServletConfig getServletConfig() {
                return servletConfig;
            }

            @Override
            public void log(String string) {
                // ignore
            }
        };

        // Mock ServletMonitor
        final ServletMonitor servletMonitor = mockery.mock(ServletMonitor.class);
        mockery.checking(new Expectations() {
            {
                allowing(servletMonitor).servletServiceFailed(with(any(WaffleException.class)));
                allowing(servletMonitor).servletServiceRequested(with(any(Map.class)));
            }
        });

        // Set up what normally would happen via "init()"
        Field actionFactoryField = WaffleServlet.class.getDeclaredField("controllerDefinitionFactory");
        actionFactoryField.setAccessible(true);
        actionFactoryField.set(servlet, controllerDefinitionFactory);

        Field servletMonitorField = WaffleServlet.class.getDeclaredField("servletMonitor");
        servletMonitorField.setAccessible(true);
        servletMonitorField.set(servlet, servletMonitor);

        servlet.service(request, response);
    }

    @SuppressWarnings("serial")
    @Test
    public void cannotServiceIfMethodDefinitionIsNull() throws Exception {
        // Mock ErrorsContext
        final ErrorsContext errorsContext = mockery.mock(ErrorsContext.class);
        final ContextContainer contextContainer = mockery.mock(ContextContainer.class);
        mockery.checking(new Expectations() {
            {
                one(contextContainer).getComponent(ErrorsContext.class);
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
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                atLeast(1).of(request).getParameterNames();
                will(returnValue(enumeration));
            }
        });

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock ActionMethodResponseHandler
        final ActionMethodResponseHandler actionMethodResponseHandler = mockery.mock(ActionMethodResponseHandler.class);
        mockery.checking(new Expectations() {
            {
                one(actionMethodResponseHandler).handle(with(same(request)), with(same(response)),
                        with(any(ActionMethodResponse.class)));
            }
        });

        // Mock Validator
        final Validator validator = mockery.mock(Validator.class);
        mockery.checking(new Expectations() {
            {
                one(validator).validate(with(any(ControllerDefinition.class)), with(any(ErrorsContext.class)));
            }
        });

        // Mock ViewDataBinder
        final ViewDataBinder viewDataBinder = mockery.mock(ViewDataBinder.class);
        mockery.checking(new Expectations() {
            {
                one(viewDataBinder).bind(with(same(request)), with(any(NonDispatchingController.class)));
            }
        });

        // Mock ControllerDefinitionFactory
        final ControllerDefinitionFactory controllerDefinitionFactory = mockery.mock(ControllerDefinitionFactory.class);
        mockery.checking(new Expectations() {
            {
                one(controllerDefinitionFactory).getControllerDefinition(with(same(request)), with(same(response)));
                will(returnValue(new ControllerDefinition("no name", nonDispatchingController, null)));
            }
        });

        // stub out what we don't want called ... execute it
        WaffleServlet servlet = new WaffleServlet(null, actionMethodResponseHandler, new SilentMonitor(),
                new OgnlControllerDataBinder(new OgnlValueConverterFinder(), null, new SilentMonitor()),
                controllerDefinitionFactory, new DefaultMessageResources(), viewDataBinder, new DefaultViewResolver(), validator) {
            @Override
            public ServletConfig getServletConfig() {
                return servletConfig;
            }

            @Override
            public void log(String string) {
                // ignore
            }
        };

        servlet.service(request, response);
    }

    @SuppressWarnings( { "serial", "unchecked" })
    @Test
    public void canHandleExceptionsInMethodInvocation() throws Exception {
        // Mock ErrorsContext
        final ErrorsContext errorsContext = mockery.mock(ErrorsContext.class);
        final ContextContainer contextContainer = mockery.mock(ContextContainer.class);
        mockery.checking(new Expectations() {
            {
                one(contextContainer).getComponent(ErrorsContext.class);
                will(returnValue(errorsContext));
                one(errorsContext).hasErrorMessages();
                will(returnValue(false));
                one(errorsContext).addErrorMessage(with(any(ErrorMessage.class)));
            }
        });

        RequestLevelContainer.set(contextContainer);

        final NonDispatchingController nonDispatchingController = new NonDispatchingController();
        List<?> list = Collections.EMPTY_LIST;
        final Enumeration<?> enumeration = Collections.enumeration(list);

        // Mock ServletContext
        final ServletConfig servletConfig = mockery.mock(ServletConfig.class);

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                atLeast(1).of(request).getParameterNames();
                will(returnValue(enumeration));
            }
        });

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // MethodDefinition
        Method method = NonDispatchingController.class.getMethod("increment");
        final MethodDefinition methodDefinition = new MethodDefinition(method);

        // Mock ActionMethodResponseHandler
        final ActionMethodResponseHandler actionMethodResponseHandler = mockery.mock(ActionMethodResponseHandler.class);
        mockery.checking(new Expectations() {
            {
                one(actionMethodResponseHandler).handle(with(same(request)), with(same(response)),
                        with(any(ActionMethodResponse.class)));
            }
        });

        // Mock Validator
        final Validator validator = mockery.mock(Validator.class);
        mockery.checking(new Expectations() {
            {
                allowing(validator).validate(with(any(ControllerDefinition.class)), with(any(ErrorsContext.class)));
            }
        });

        // Mock RequestAttributeBinder
        final ViewDataBinder viewDataBinder = mockery.mock(ViewDataBinder.class);
        mockery.checking(new Expectations() {
            {
                one(viewDataBinder).bind(with(same(request)), with(any(NonDispatchingController.class)));
            }
        });

        // Mock ControllerDefinitionFactory
        final ControllerDefinitionFactory controllerDefinitionFactory = mockery.mock(ControllerDefinitionFactory.class);
        mockery.checking(new Expectations() {
            {
                one(controllerDefinitionFactory).getControllerDefinition(with(same(request)), with(same(response)));
                will(returnValue(new ControllerDefinition("no name", nonDispatchingController, methodDefinition)));
            }
        });

        // stub out what we don't want called ... execute it
        WaffleServlet servlet = new WaffleServlet(null, actionMethodResponseHandler, new SilentMonitor(),
                new OgnlControllerDataBinder(new OgnlValueConverterFinder(), null, new SilentMonitor()),
                controllerDefinitionFactory, new DefaultMessageResources(), viewDataBinder, new DefaultViewResolver(), validator) {
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
        final ActionMethodInvocationException actionMethodInvocationException = new ActionMethodInvocationException(
                "fake from test");
        mockery.checking(new Expectations() {
            {
                one(actionMethodExecutor).execute(with(any(ActionMethodResponse.class)),
                        with(any(ControllerDefinition.class)));
                will(throwException(actionMethodInvocationException));
            }
        });

        // Mock ServletMonitor
        final ServletMonitor servletMonitor = mockery.mock(ServletMonitor.class);
        mockery.checking(new Expectations() {
            {
                allowing(servletMonitor).servletServiceRequested(with(any(Map.class)));
                allowing(servletMonitor).actionMethodInvocationFailed(actionMethodInvocationException);
            }
        });

        // Set up what normally would happen via "init()"
        Field controllerDataBinderField = WaffleServlet.class.getDeclaredField("controllerDataBinder");
        controllerDataBinderField.setAccessible(true);
        controllerDataBinderField.set(servlet, new OgnlControllerDataBinder(new OgnlValueConverterFinder(), null,
                new SilentMonitor()));

        Field actionMethodExecutorField = WaffleServlet.class.getDeclaredField("actionMethodExecutor");
        actionMethodExecutorField.setAccessible(true);
        actionMethodExecutorField.set(servlet, actionMethodExecutor);

        Field servletMonitorField = WaffleServlet.class.getDeclaredField("servletMonitor");
        servletMonitorField.setAccessible(true);
        servletMonitorField.set(servlet, servletMonitor);

        servlet.service(request, response);
    }

    @SuppressWarnings("serial")
    @Test
    public void canBuildViewToReferrer() throws Exception {
        WaffleServlet servlet = new WaffleServlet(){
            public String getInitParameter(String name) {
                if ( name.equals(Constants.VIEW_PREFIX_KEY) ){
                    return "prefix-";    
                } else if ( name.equals(Constants.VIEW_SUFFIX_KEY) ){
                    return "-suffix";    
                } else {
                    return null;
                }
            }
        };
        
        // Set up what normally would happen via "init()"
        ViewResolver viewResolver = new DefaultViewResolver();
        Field viewResolverField = WaffleServlet.class.getDeclaredField("viewResolver");
        viewResolverField.setAccessible(true);
        viewResolverField.set(servlet, viewResolver);
        servlet.configureViewProperties();

        ControllerDefinition controllerDefinition = new ControllerDefinition("foobar", null, null);
        View view = servlet.buildView(controllerDefinition);
        assertEquals("prefix-foobar-suffix", viewResolver.resolve(view));
    }

    @SuppressWarnings("serial")
    @Test(expected = WaffleException.class)
    public void cannotInitWithoutParameter() throws ServletException {
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {
            {
                one(servletContext).getAttribute(ComponentRegistry.class.getName());
                will(returnValue(null));
            }
        });

        // Mock ServletConfig
        final ServletConfig servletConfig = mockery.mock(ServletConfig.class);
        mockery.checking(new Expectations() {
            {
                one(servletConfig).getInitParameter(Constants.VIEW_PREFIX_KEY);
                will(returnValue("/WEB-INF/jsp"));
                one(servletConfig).getInitParameter(Constants.VIEW_SUFFIX_KEY);
                one(servletConfig).getInitParameter(Constants.ERRORS_VIEW_KEY);
                will(returnValue("errors"));
            }
        });

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
