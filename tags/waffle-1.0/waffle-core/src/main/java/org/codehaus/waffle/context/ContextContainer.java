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
package org.codehaus.waffle.context;

import org.codehaus.waffle.Startable;

import java.util.Collection;
import java.io.Serializable;

public interface ContextContainer extends Startable, Serializable {

    void dispose();

    void registerComponentInstance(Object instance);

    Object getComponentInstance(Object key);

    <T> T getComponentInstanceOfType(Class<T> type);

    <T> Collection<T> getAllComponentInstancesOfType(Class<T> type);

    void validateComponentInstances();

    Object getDelegate();

}
