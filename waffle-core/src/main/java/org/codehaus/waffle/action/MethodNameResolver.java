/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementation of this interface will be able to determine the action method name that is to be invoked.
 * 
 * @author Michael Ward
 */
public interface MethodNameResolver {

    /**
     * Find the method name to be invoked
     *
     * @param request is the current request
     * @return the name of the method that is to be invoked
     */
    String resolve(HttpServletRequest request);
}
