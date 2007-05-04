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

import java.util.Map;

/**
 * @author Michael Ward
 */
public class RedirectView extends View {
    private final Map model;

    public RedirectView(String value, Object fromController, Map model) {
        super(value, fromController);
        this.model = model;
    }

    public Map getModel() {
        return model;
    }
}
