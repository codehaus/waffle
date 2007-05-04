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
package com.thoughtworks.waffle.view;

/**
 * The default view resolver
 *
 * @author Michael Ward
 */
public class DefaultViewResolver implements ViewResolver {

    /**
     * The Default ViewResolver simply return the vale of the View being reolved.
     */
    public String resolve(View view) {
        return view.getValue();
    }
}
