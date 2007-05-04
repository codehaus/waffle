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
package com.thoughtworks.waffle.context;

import com.thoughtworks.waffle.Startable;

import java.util.Collection;

public interface ContextContainer extends Startable {

    void dispose();

    void registerComponentInstance(Object instance);

    Object getComponentInstance(Object key);

    <T> T getComponentInstanceOfType(Class<T> type);

    <T> Collection<T> getAllComponentInstancesOfType(Class<T> type);

    void validateComponentInstances();

    Object getDelegate();

}
