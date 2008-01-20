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
 * 
 * Represents the value of the view that the resolver will dispatch.
 * View holds:
 * <ol>
 *   <li>the value from the controller method</li>
 *   <li>the controller object </li>
 * </ol>
 * this allows for more granular decisions on how to handle a View
 *
 * @author Michael Ward
 */
public class View {
    private final String value;
    private final Object controller;

    /**
     * Creates a View
     * 
     * @param value represents the name of the View to be resolved
     * @param controller the controller where the view originated from
     */
    public View(String value, Object controller) {
        this.value = value;
        this.controller = controller;
    }

    /**
     * Returns the view value.  The term "value" is used to be purposely open ended.
     * 
     * @return The View value
     */
    public String getValue() {
        return value;
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
