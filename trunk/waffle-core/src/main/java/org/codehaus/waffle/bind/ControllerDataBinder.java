/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind;

import org.codehaus.waffle.validation.ErrorsContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementor of this interface are responsible for binding the values from the request to the controller.
 * 
 * @author Michael Ward
 */
public interface ControllerDataBinder {

    /**
     * Bind parameters values from the request to the controller
     * 
     * @param request the HttpServletRequest containing the parameter values
     * @param response the HttpServletResponse
     * @param errorsContext the ErrorsContext
     * @param controller the controller instance
     */
    void bind(HttpServletRequest request, HttpServletResponse response, ErrorsContext errorsContext, Object controller);

}
