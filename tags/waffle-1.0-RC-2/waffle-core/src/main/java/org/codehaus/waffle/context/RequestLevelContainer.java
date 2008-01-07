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
package org.codehaus.waffle.context;

public class RequestLevelContainer {
    private final static ThreadLocal<ContextContainer> REQUEST_CONTAINER = new ThreadLocal<ContextContainer>();

    private RequestLevelContainer() {
    }

    public static ContextContainer get() {
        return REQUEST_CONTAINER.get();
    }

    public static void set(ContextContainer container) {
        REQUEST_CONTAINER.set(container);
    }
}
