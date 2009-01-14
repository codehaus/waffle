/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.controller;

import org.codehaus.waffle.i18n.MessagesContext;
import org.codehaus.waffle.context.ContextContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementations of this interface are responsible for creating and
 * maintaining the "controller" objects (pojo's).
 *
 * @author Michael Ward
 */
public interface ControllerDefinitionFactory {

    /**
     * Implementors of this method should decipher the Servlet request passed
     * in and provide the associated controller instance (pojo).  The instance of
     * the Controller object and the key the controller was retrieved with are returned
     * in an ControllerDefinition.
     */
    ControllerDefinition getControllerDefinition(HttpServletRequest servletRequest,
                                                 HttpServletResponse response, MessagesContext messageContext, ContextContainer requestLevelContainer);
}
