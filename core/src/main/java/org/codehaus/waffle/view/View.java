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
package org.codehaus.waffle.view;

/**
 * Represents the value of the view, View resolver will dispatch
 * <p/>
 * View should hold:
 * - the value from that action method
 * - The action
 * <p/>
 * this allows for more granular decisions on how to handle a View
 *
 * @author Michael Ward
 */
public class View {
    private final String value;
    private final Object fromController;

    public View(String name, Object fromAction) {
        this.value = name;
        this.fromController = fromAction;
    }

    // the term "value" is used to be purposely open ended
    public String getValue() {
        return value;
    }

    /**
     * Represent which Controller this View originated from
     */
    public Object getFromController() {
        return fromController;
    }

}
