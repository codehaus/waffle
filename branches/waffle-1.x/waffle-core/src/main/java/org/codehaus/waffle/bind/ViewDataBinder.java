/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementors of this class allow for properties from a controller to be exposed to the view as request attributes.
 * 
 * @author Michael Ward
 */
public interface ViewDataBinder {

    void bind(HttpServletRequest request, Object controller);
}
