/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.controller;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface to lookup the controller name in the request
 * 
 * @author Michael Ward
 */
public interface ControllerNameResolver {

    String findControllerName(HttpServletRequest request);
}
