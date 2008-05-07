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
package org.codehaus.waffle.action.intercept;

/**
 * <p>Identifies a {@link MethodInterceptor} so that it will be evaluated in the correct order</p>
 */
public interface Sortable {

    /**
     * Defines the order the implementing MethodInterceptor should be evaluated (in comparison to other MethodInterceptors).
     *
     * @return the value representing its place in line
     */
    Integer getIndex();
}
