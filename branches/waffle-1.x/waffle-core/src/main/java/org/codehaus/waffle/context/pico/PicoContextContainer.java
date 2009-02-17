/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context.pico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.i18n.MessageResources;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoException;
import org.picocontainer.behaviors.Caching;

@SuppressWarnings("serial")
public class PicoContextContainer implements ContextContainer {
    private final MutablePicoContainer delegate;
    private final MessageResources messageResources;

    public PicoContextContainer() {
        this(new DefaultPicoContainer(new Caching()));
    }

    public PicoContextContainer(MutablePicoContainer delegate) {
        this(delegate, new DefaultMessageResources());
    }

    public PicoContextContainer(MutablePicoContainer delegate, MessageResources messageResources) {
        this.delegate = delegate;
        this.messageResources = messageResources;
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
            String message = messageResources.getMessageWithDefault("componentInstantiationFailed",
                    "Failed to instantiate component for key ''{0}''", key);
            throw new WaffleException(message, e);
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
