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
package org.codehaus.waffle.context.pico;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.context.ContextContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoException;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PicoContextContainer implements ContextContainer {
    private final MutablePicoContainer delegate;

    public PicoContextContainer() {
        this(new DefaultPicoContainer());
    }

    public PicoContextContainer(MutablePicoContainer delegate) {
        this.delegate = delegate;
    }

    public void registerComponentInstance(Object instance) {
        delegate.registerComponentInstance(instance);
    }

    public void start() {
        delegate.start();
    }

    public void stop() {
        delegate.stop();
    }

    public void dispose() {
        delegate.dispose();
    }

    public Object getComponentInstance(Object key) {
        try {
            return delegate.getComponentInstance(key);
        } catch (PicoException e) {
            throw new WaffleException("Unable to construct component '" + key  +"'.", e);
        }
    }

    @SuppressWarnings({"unchecked"})
    public <T> T getComponentInstanceOfType(Class<T> type) {
        return (T) delegate.getComponentInstanceOfType(type);
    }

    public <T> Collection<T> getAllComponentInstancesOfType(Class<T> type) {
        List<T> all = new ArrayList<T>();
        traverseContainerHeirarchy(delegate, type, all);

        return all;
    }
    
    public void validateComponentInstances() {
        delegate.getComponentInstances();
    }

    @SuppressWarnings({"unchecked"})
    private <T> void traverseContainerHeirarchy(PicoContainer container, Class<T> type, List<T> collection) {
        collection.addAll(container.getComponentInstancesOfType(type));

        PicoContainer parent = container.getParent();
        if (parent != null) {
            traverseContainerHeirarchy(parent, type, collection);
        }
    }

    public MutablePicoContainer getDelegate() {
        return delegate;
    }

}