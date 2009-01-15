/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context;

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
import org.picocontainer.containers.AbstractDelegatingPicoContainer;
import org.picocontainer.containers.AbstractDelegatingMutablePicoContainer;
import org.picocontainer.behaviors.Caching;

@SuppressWarnings("serial")
public class ContextContainer extends AbstractDelegatingMutablePicoContainer {
    private final MessageResources messageResources;

    public ContextContainer() {
        this(new DefaultPicoContainer(new Caching()));
    }

    public ContextContainer(MutablePicoContainer delegate) {
        this(delegate, new DefaultMessageResources());
    }

    public ContextContainer(MutablePicoContainer delegate, MessageResources messageResources) {
        super(delegate);
        this.messageResources = messageResources;
    }

    public Object getComponent(Object key) {
        try {
            return getDelegate().getComponent(key);
        } catch (PicoException e) {
            String message = messageResources.getMessageWithDefault("componentInstantiationFailed",
                    "Failed to instantiate component for key ''{0}''", key);
            throw new WaffleException(message, e);
        }
    }

    public <T> List<T> getComponents(Class<T> type) {
        List<T> all = new ArrayList<T>();
        traverseContainerHeirarchy(getDelegate(), type, all);

        return all;
    }

    public void validateComponentInstances() {
        getDelegate().getComponents();
    }

    private <T> void traverseContainerHeirarchy(PicoContainer container, Class<T> type, List<T> collection) {
        collection.addAll(container.getComponents(type));

        PicoContainer parent = container.getParent();
        if (parent != null) {
            traverseContainerHeirarchy(parent, type, collection);
        }
    }

}
