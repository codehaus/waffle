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
package org.codehaus.waffle.view;

/**
 * Represents the view that the resolver will dispatch.  View holds:
 * <ol>
 *   <li>the path of the view</li>
 *   <li>the controller object </li>
 * </ol>
 * which allows for more granular decisions on how to handle a View.
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class View {
    private final String path;
    private final Object controller;

    /**
     * Creates a View
     * 
     * @param path represents the path of the View to be resolved
     * @param controller the controller where the view originated from
     */
    public View(String path, Object controller) {
        this.path = path;
        this.controller = controller;
    }

    /**
     * Returns the view path
     * 
     * @return The View path
     */
    public String getPath() {
        return path;
    }
    
    /**
     * @deprecated Use #getPath()
     */
    public String getValue() {
        return getPath();
    }

    /**
     * Returns the Controller this View originated from
     *
     * @return The Controller instance
     */
    public Object getController() {
        return controller;
    }

}
