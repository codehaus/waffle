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
 * The view resolver determines the path the next view
 *
 * @author Michael Ward
 */
public interface ViewResolver {

    /**
     * Resolves the view by return the path to the next view
     *
     * @param view the View
     * @return The path to the next View.
     */
    String resolve(View view);
}
