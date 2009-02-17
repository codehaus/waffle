/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action;

import org.codehaus.waffle.i18n.MessagesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Finds method definitions in the controller using the parameters specified in the request
 * 
 * @author Michael Ward
 */
public interface MethodDefinitionFinder {

    /**
     * Returns a method definition of a given controller
     * 
     * @param controller the controller Object
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @param messageContext
     * @return The MethodDefinition found
     */
    MethodDefinition find(Object controller,
                          HttpServletRequest request,
                          HttpServletResponse response, MessagesContext messageContext);

}
