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
package org.codehaus.waffle.registrar;

/**
 * Simple abstract implementation which manages the associated <i>key</i>.
 *
 * @author Michael Ward
 */
public abstract class AbstractReference implements Reference {
    private Object key;

    public AbstractReference(Object key) {
        this.key = key;
    }

    public Object getKey() {
        return key;
    }
}
