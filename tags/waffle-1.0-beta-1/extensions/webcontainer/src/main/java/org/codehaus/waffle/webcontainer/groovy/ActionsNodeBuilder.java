/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 *****************************************************************************/
package org.codehaus.waffle.webcontainer.groovy;

import java.util.Map;

import groovy.util.NodeBuilder;

import org.codehaus.waffle.registrar.Registrar;
import org.nanocontainer.DefaultNanoContainer;
import org.nanocontainer.NanoContainer;
import org.nanocontainer.webcontainer.PicoContext;
import org.picocontainer.PicoContainer;

public class ActionsNodeBuilder extends NodeBuilder {
    protected final PicoContainer parentContainer;
    protected final PicoContext context;

    protected ActionsNodeBuilder(PicoContainer parentContainer, PicoContext context) {
        this.parentContainer = parentContainer;
        this.context = context;
    }

    protected Object buildInstance(String actionClassName) {
        NanoContainer factory = new DefaultNanoContainer();
        factory.getPico().registerComponentInstance(PicoContext.class, context);
        factory.getPico().registerComponentInstance(PicoContainer.class, parentContainer);
        try {
            factory.registerComponentImplementation("action", actionClassName);
            return (NodeBuilder) factory.getPico().getComponentInstance("action");
        } catch (ClassNotFoundException e) {
            throw new org.nanocontainer.script.BuilderClassNotFoundException(actionClassName + " class name not found", e);
        }
    }
    
    protected void registerAction(Map attributes) {
        Registrar registrar = (Registrar) parentContainer.getComponentInstanceOfType(Registrar.class);
        Object clazz = attributes.remove("class"); // could be Class or String
        if ( clazz instanceof Class ){
            registrar.register((Class)clazz, new Object[]{});    
        } else {
            registrar.register(buildInstance((String) clazz).getClass(), new Object[]{});                    
        }
    }
    

}
