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

import static org.codehaus.waffle.Constants.VIEW_PREFIX_KEY;
import static org.codehaus.waffle.Constants.VIEW_SUFFIX_KEY;
import static org.codehaus.waffle.Constants.ERRORS_KEY;
import static org.codehaus.waffle.Constants.METHOD_INVOCATION_ERROR_PAGE;

import org.codehaus.waffle.WaffleComponentRegistry;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;
import org.codehaus.waffle.action.ActionMethodExecutor;
import org.codehaus.waffle.action.ActionMethodResponseHandler;
import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.*;
import org.codehaus.waffle.bind.DataBinder;
import org.codehaus.waffle.validation.DefaultErrorsContext;
import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.Validator;
import org.codehaus.waffle.view.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Waffle's FrontController for handling user requests.
 *
 * @author Michael Ward
 */
public class WaffleServlet extends HttpServlet {
    private ControllerDefinitionFactory controllerDefinitionFactory;
    private DataBinder dataBinder;
    private ActionMethodExecutor actionMethodExecutor;
    private ActionMethodResponseHandler actionMethodResponseHandler;
    private Validator validator;
    private String viewPrefix;
    private String viewSuffix;
    private boolean depsDone = false;
    private String methodInvocationErrorPage;

    public WaffleServlet() {
    }

    public WaffleServlet(ControllerDefinitionFactory controllerDefinitionFactory,
                         DataBinder dataBinder,
                         ActionMethodExecutor actionMethodExecutor,
                         ActionMethodResponseHandler actionMethodResponseHandler,
                         Validator validator) {
        this.controllerDefinitionFactory = controllerDefinitionFactory;
        this.dataBinder = dataBinder;
        this.actionMethodExecutor = actionMethodExecutor;
        this.actionMethodResponseHandler = actionMethodResponseHandler;
        this.validator = validator;
        depsDone = true;
    }

    public void init() throws ServletException {
        viewPrefix = getInitParameter(VIEW_PREFIX_KEY);
        if (viewPrefix == null || viewPrefix.equals("")) {
            viewPrefix = "/"; // default
        }

        viewSuffix = getInitParameter(VIEW_SUFFIX_KEY);
        if (viewSuffix == null || viewSuffix.equals("")) {
            viewSuffix = ".jspx";
        }
        methodInvocationErrorPage = getInitParameter(METHOD_INVOCATION_ERROR_PAGE);

        if (!depsDone) {
            // Obtain required components from the Component Registry
            WaffleComponentRegistry componentRegistry = ServletContextHelper
                    .getWaffleComponentRegistry(getServletContext());
            controllerDefinitionFactory = componentRegistry.getControllerDefinitionFactory();
            dataBinder = componentRegistry.getDataBinder();
            actionMethodExecutor = componentRegistry.getActionMethodExecutor();
            actionMethodResponseHandler = componentRegistry.getActionMethodResponseHandler();
            validator = componentRegistry.getValidator();
        }
    }

    /**
     * Obtain the controller the user is requesting.
     */
    protected ControllerDefinition getControllerDefinition(HttpServletRequest request,
                                                   HttpServletResponse response) throws ServletException {
        ControllerDefinition controllerDefinition = controllerDefinitionFactory.getControllerDefinition(request, response);
        if (controllerDefinition.getController() == null) {
            throw new ServletException("Unable to locate the Waffle-Controller: " + request.getServletPath());
        }

        return controllerDefinition;

    }

    /**
     * Responsible for servicing the requests from the users.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {
        ErrorsContext errorsContext = new DefaultErrorsContext();
        request.setAttribute(ERRORS_KEY, errorsContext);

        ControllerDefinition controllerDefinition = getControllerDefinition(request, response);
        dataBinder.bind(request, response, errorsContext, controllerDefinition.getController());
        validator.validate(controllerDefinition, errorsContext);

        try {
            ActionMethodResponse actionMethodResponse = new ActionMethodResponse();
            MethodDefinition methodDefinition = controllerDefinition.getMethodDefinition();

            if (errorsContext.hasErrorMessages() || methodDefinition == null) {
                buildViewToReferrer(controllerDefinition, actionMethodResponse);
            } else {
                actionMethodExecutor.execute(actionMethodResponse, controllerDefinition);

                if (errorsContext.hasErrorMessages() || actionMethodResponse.getReturnValue() == null) {
                    // Null and VOID need to build a view back to the referring page
                    buildViewToReferrer(controllerDefinition, actionMethodResponse);
                }
            }

            actionMethodResponseHandler.handle(request, response, actionMethodResponse);
        } catch (MethodInvocationException e) {
            log(e.getMessage());
            if (methodInvocationErrorPage != null && !methodInvocationErrorPage.equals("")) {
                response.sendRedirect(methodInvocationErrorPage);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    /**
     * Build a view back to the referring page (use the Controller's name as the View name).
     */
    protected void buildViewToReferrer(ControllerDefinition controllerDefinition, ActionMethodResponse actionMethodResponse) {
        String controllerValue = viewPrefix + controllerDefinition.getName() + viewSuffix;
        View view = new View(controllerValue, controllerDefinition.getController());
        actionMethodResponse.setReturnValue(view);
    }

}
