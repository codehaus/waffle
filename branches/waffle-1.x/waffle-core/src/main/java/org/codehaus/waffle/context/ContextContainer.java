/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context;

import org.codehaus.waffle.Startable;

import java.util.Collection;
import java.io.Serializable;

public interface ContextContainer extends Startable, Serializable {

    void dispose();

    void registerComponentInstance(Object instance);

    Object getComponentInstance(Object key);

    <T> T getComponent(Class<T> type);

    <T> Collection<T> getAllComponentInstancesOfType(Class<T> type);

    void validateComponentInstances();

    Object getDelegate();

}
