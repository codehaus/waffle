/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context.pico;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.context.ContextContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoException;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.behaviors.Caching;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("serial")
public class PicoContextContainer implements ContextContainer {
    private final MutablePicoContainer delegate;

    public PicoContextContainer() {
        this(new DefaultPicoContainer(new Caching()));
    }

    public PicoContextContainer(MutablePicoContainer delegate) {
        this.delegate = delegate;
    }

    public void registerComponentInstance(Object instance) {
        delegate.addComponent(instance);
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
            return delegate.getComponent(key);
        } catch (PicoException e) {
            throw new WaffleException("Unable to construct component '" + key  +"'.", e);
        }
    }

    public <T> T getComponent(Class<T> type) {
        return delegate.getComponent(type);
    }

    public <T> Collection<T> getAllComponentInstancesOfType(Class<T> type) {
        List<T> all = new ArrayList<T>();
        traverseContainerHeirarchy(delegate, type, all);

        return all;
    }
    
    public void validateComponentInstances() {
        delegate.getComponents();
    }

    private <T> void traverseContainerHeirarchy(PicoContainer container, Class<T> type, List<T> collection) {
        collection.addAll(container.getComponents(type));

        PicoContainer parent = container.getParent();
        if (parent != null) {
            traverseContainerHeirarchy(parent, type, collection);
        }
    }

    public MutablePicoContainer getDelegate() {
        return delegate;
    }

}
