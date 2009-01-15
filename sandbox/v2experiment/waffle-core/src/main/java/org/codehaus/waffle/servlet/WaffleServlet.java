/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.servlet;

import static java.util.Arrays.asList;
import static org.codehaus.waffle.Constants.ERRORS_VIEW_KEY;
import static org.codehaus.waffle.Constants.VIEW_PREFIX_KEY;
import static org.codehaus.waffle.Constants.VIEW_SUFFIX_KEY;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.action.ActionMethodExecutor;
import org.codehaus.waffle.action.ActionMethodInvocationException;
import org.codehaus.waffle.action.ActionMethodResponse;
import org.codehaus.waffle.action.ActionMethodResponseHandler;
import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.intercept.MethodInterceptor;
import org.codehaus.waffle.action.annotation.PRG;
import org.codehaus.waffle.bind.ControllerDataBinder;
import org.codehaus.waffle.bind.ViewDataBinder;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.RequestLevelContainer;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.i18n.MessagesContext;
import org.codehaus.waffle.monitor.ServletMonitor;
import org.codehaus.waffle.monitor.ValidationMonitor;
import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.GlobalErrorMessage;
import org.codehaus.waffle.validation.Validator;
import org.codehaus.waffle.validation.ValidatorConfiguration;
import org.codehaus.waffle.validation.DefaultValidatorConfiguration;
import org.codehaus.waffle.view.RedirectView;
import org.codehaus.waffle.view.View;
import org.codehaus.waffle.view.ViewResolver;
import org.picocontainer.MutablePicoContainer;

/**
 * Waffle's FrontController for handling user requests.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
@SuppressWarnings("serial")
public class WaffleServlet extends HttpServlet {

    private static final String EMPTY = "";
    private static final String POST = "POST";
    private ActionMethodExecutor actionMethodExecutor;
    private ActionMethodResponseHandler actionMethodResponseHandler;
    private ControllerDefinitionFactory controllerDefinitionFactory;
    private ControllerDataBinder controllerDataBinder;
    private MessageResources messageResources;
    private ViewDataBinder viewDataBinder;
    private ViewResolver viewResolver;
    private Validator validator;
    private ServletMonitor servletMonitor;
    private boolean componentsRetrieved = false;

    /**
     * Default constructor used by servlet container
     */
    public WaffleServlet() {
        // initialisation will be performed in init() method
    }

    /**
     * Constructor required by builder and useful for testing
     * 
     * @param actionMethodExecutor
     * @param actionMethodResponseHandler
     * @param servletMonitor
     * @param controllerDataBinder
     * @param controllerDefinitionFactory
     * @param messageResources
     * @param viewDataBinder
     * @param viewResolver
     * @param validator
     */
    public WaffleServlet(ActionMethodExecutor actionMethodExecutor,
            ActionMethodResponseHandler actionMethodResponseHandler, ServletMonitor servletMonitor,
            ControllerDataBinder controllerDataBinder, ControllerDefinitionFactory controllerDefinitionFactory,
            MessageResources messageResources, ViewDataBinder viewDataBinder, ViewResolver viewResolver,
            Validator validator) {
        this.actionMethodExecutor = actionMethodExecutor;
        this.actionMethodResponseHandler = actionMethodResponseHandler;
        this.servletMonitor = servletMonitor;
        this.controllerDataBinder = controllerDataBinder;
        this.controllerDefinitionFactory = controllerDefinitionFactory;
        this.messageResources = messageResources;
        this.viewDataBinder = viewDataBinder;
        this.viewResolver = viewResolver;
        this.validator = validator;
        componentsRetrieved = true;
    }

    public void init() throws ServletException {
        if (!componentsRetrieved) {
            // Retrieve instance components from the ComponentRegistry
            ComponentRegistry registry = getComponentRegistry();
            actionMethodExecutor = registry.getActionMethodExecutor();
            actionMethodResponseHandler = registry.getActionMethodResponseHandler();
            controllerDefinitionFactory = registry.getControllerDefinitionFactory();
            controllerDataBinder = registry.getControllerDataBinder();
            messageResources = registry.getMessageResources();
            viewDataBinder = registry.getViewDataBinder();
            viewResolver = registry.getViewResolver();
            validator = registry.getValidator();
            servletMonitor = registry.getServletMonitor();
        }

        configureViewProperties();
        servletMonitor.servletInitialized(this);
    }

    protected void configureViewProperties() {
        Properties viewProperties = new Properties();
        setViewPropertyIfFound(viewProperties, VIEW_PREFIX_KEY);
        setViewPropertyIfFound(viewProperties, VIEW_SUFFIX_KEY);
        setViewPropertyIfFound(viewProperties, ERRORS_VIEW_KEY);
        viewResolver.configureViews(viewProperties);
    }

    private void setViewPropertyIfFound(Properties viewProperties, String key) {
        String initParam = initParam(key, null);
        if (initParam != null) {
            viewProperties.setProperty(key, initParam);
        }
    }

    private String initParam(String key, String defaultValue) {
        String value = getInitParameter(key);
        if (value == null || value.equals(EMPTY)) {
            value = defaultValue; // default
        }
        return value;
    }

    private ComponentRegistry getComponentRegistry() {
        return ServletContextHelper.getComponentRegistry(getServletContext());
    }

    /**
     * Responsible for servicing the requests from the users.
     * 
     * @param request the HttpServletResponse
     * @param response the HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        servletMonitor.servletServiceRequested(parametersOf(request));
        MutablePicoContainer requestContainer = RequestLevelContainer.get();
        ErrorsContext errorsContext = requestContainer.getComponent(ErrorsContext.class);
        Collection<MethodInterceptor> methodInterceptors = requestContainer.getComponents(MethodInterceptor.class);
        MessagesContext messageContext = requestContainer.getComponent(MessagesContext.class);

        ValidationMonitor validationMonitor = requestContainer.getComponent(ValidationMonitor.class);
        ValidatorConfiguration validatorConfiguration = requestContainer.getComponent(ValidatorConfiguration.class);
        if (validatorConfiguration == null) {
            validatorConfiguration = new DefaultValidatorConfiguration();
        }

        ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
        View view = null;
        try {

            ControllerDefinition controllerDefinition = controllerDefinitionFactory.getControllerDefinition(request,
                    response, messageContext, requestContainer);
            controllerDataBinder.bind(request, response, errorsContext, controllerDefinition.getController());
            String controllerName = controllerDefinition.getName();
            Object controllerValidator;
            String controllerValidatorName = controllerName + validatorConfiguration.getSuffix();
            controllerValidator = requestContainer.getComponent(controllerValidatorName);

            if (controllerValidator == null) {
                // default to use controller as validator
                controllerValidator = requestContainer.getComponent(controllerName);
                validationMonitor.controllerValidatorNotFound(controllerValidatorName, controllerName);
            }

            validator.validate(controllerDefinition, errorsContext, controllerValidator);
            try {

                if (errorsContext.hasErrorMessages() || noMethodDefinition(controllerDefinition)) {
                    view = buildView(controllerDefinition);
                } else {
                    actionMethodExecutor.execute(actionMethodResponse, controllerDefinition, methodInterceptors);

                    if (errorsContext.hasErrorMessages()) {
                        view = buildView(controllerDefinition);
                    } else if (actionMethodResponse.getReturnValue() == null) {
                        // Null or VOID indicate a Waffle convention (return to referring page)
                        // unless PRG is disabled
                        if (request.getMethod().equalsIgnoreCase(POST)) {
                            if (usePRG(controllerDefinition.getMethodDefinition())) {
                                // PRG (Post/Redirect/Get): see http://en.wikipedia.org/wiki/Post/Redirect/Get
                                view = buildRedirectingView(request, controllerDefinition);
                            } else {
                                // PRG is disabled
                                view = buildView(controllerDefinition);
                            }
                        } else { // was a GET
                            view = buildView(controllerDefinition);
                        }
                    }
                }

            } catch (ActionMethodInvocationException e) {
                servletMonitor.actionMethodInvocationFailed(e);
                String message = messageResources.getMessageWithDefault("actionMethodInvocationFailed",
                        "Action method invocation failed for controller ''{0}''", controllerDefinition);
                errorsContext.addErrorMessage(new GlobalErrorMessage(message, e));
                view = buildActionMethodFailureView(controllerDefinition);
            }
            viewDataBinder.bind(request, controllerDefinition.getController());
        } catch (WaffleException e) {
            servletMonitor.servletServiceFailed(e);
            errorsContext.addErrorMessage(new GlobalErrorMessage(e.getMessage(), e));
            view = buildErrorsView();
        }

        if (view != null) {
            actionMethodResponse.setReturnValue(view);
        }
        actionMethodResponseHandler.handle(request, response, actionMethodResponse);
    }

    private boolean noMethodDefinition(ControllerDefinition controllerDefinition) {
        return controllerDefinition.getMethodDefinition() == null;
    }

    @SuppressWarnings("unchecked")
    private Map<String, List<String>> parametersOf(HttpServletRequest request) {
        Map<String, List<String>> parameters = new HashMap<String, List<String>>();
        for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
            String name = e.nextElement();
            parameters.put(name, asList(request.getParameterValues(name)));
        }
        return parameters;
    }

    /**
     * Determine if PRG paradigm is used from the @PRG annotation of the action method
     * 
     * @param methodDefinition the MethodDefinition
     * @return A boolean flag, defaults to <code>true</code> if no annotation found
     */
    private boolean usePRG(MethodDefinition methodDefinition) {
        Method method = methodDefinition.getMethod();
        // look for PRG annotation
        PRG prg = method.getAnnotation(PRG.class);
        if (prg != null) {
            return prg.value();
        }
        // else default to true
        return true;
    }

    /**
     * Build a view back to the referring page, using the Controller's name as the View name.
     * 
     * @param controllerDefinition the ControllerDefinition
     * @return The View
     */
    protected View buildView(ControllerDefinition controllerDefinition) {
        return new View(controllerDefinition);
    }

    /**
     * Build redirecting view, used by PRG paradigm.
     * 
     * @param request the request
     * @param controllerDefinition the ControllerDefinition
     * @return The RedirectView
     */
    protected View buildRedirectingView(HttpServletRequest request, ControllerDefinition controllerDefinition) {
        return new RedirectView(request.getRequestURL().toString());
    }

    /**
     * Builds the view for action method failures, by default the referring view. The user can extend and override
     * behaviour, eg to throw a ServletException.
     * 
     * @param controllerDefinition the ControllerDefinition
     * @return The referring View
     * @throws ServletException if required
     */
    protected View buildActionMethodFailureView(ControllerDefinition controllerDefinition) throws ServletException {
        return buildView(controllerDefinition);
    }

    /**
     * Builds the errors view, for cases in which the context container or the controller are not found. The user can
     * extend and override behaviour, eg to throw a ServletException.
     * 
     * @return The referring View
     * @throws ServletException if required
     */
    protected View buildErrorsView() throws ServletException {
        return buildView(new ControllerDefinition(ERRORS_VIEW_KEY, null, null));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[WaffleServlet ");
        sb.append("actionMethodExecutor=");
        sb.append(actionMethodExecutor);
        sb.append(", actionMethodResponseHandler=");
        sb.append(actionMethodResponseHandler);
        sb.append(", controllerDefinitionFactory=");
        sb.append(controllerDefinitionFactory);
        sb.append(", controllerDataBinder=");
        sb.append(controllerDataBinder);
        sb.append(", messageResources=");
        sb.append(messageResources);
        sb.append(", viewDataBinder=");
        sb.append(viewDataBinder);
        sb.append(", viewResolver=");
        sb.append(viewResolver);
        sb.append(", validator=");
        sb.append(validator);
        sb.append("]");
        return sb.toString();
    }


}
