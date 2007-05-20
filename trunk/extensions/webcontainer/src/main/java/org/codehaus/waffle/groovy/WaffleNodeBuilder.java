/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 *****************************************************************************/

package org.codehaus.waffle.groovy;

import groovy.util.NodeBuilder;

import java.util.Map;

import org.codehaus.waffle.context.pico.PicoWaffleContextListener;
import org.codehaus.waffle.servlet.WaffleServlet;
import org.nanocontainer.script.NanoContainerMarkupException;
import org.nanocontainer.webcontainer.PicoContextHandler;
import org.nanocontainer.webcontainer.PicoServletHolder;
import org.picocontainer.PicoContainer;

public class WaffleNodeBuilder extends NodeBuilder {
    private final PicoContainer parentContainer;
    private final PicoContextHandler context;
    
    public WaffleNodeBuilder(PicoContainer parentContainer, PicoContextHandler context, Map attributes) {
        this.parentContainer = parentContainer;
        this.context = context;

        context.addListener(PicoWaffleContextListener.class);
        String servletSuffix = (String) attributes.remove("servletSuffix");
        if (servletSuffix == null || servletSuffix.equals("")) {
            servletSuffix = "*.action";
        }

        PicoServletHolder holder = context.addServletWithMapping(WaffleServlet.class,"");
        String viewSuffix = (String) attributes.remove("viewSuffix");
        if (viewSuffix != null && !viewSuffix.equals("")) {
            holder.setInitParameter("view.suffix", viewSuffix);
        }
    }

    public Object createNode(Object name, Map attributes) throws NanoContainerMarkupException {
        if (name.equals("actionRegistrar")) {
            return new ActionRegistrarNodeBuilder(parentContainer, attributes.remove("class"), context);
        } else if (name.equals("requestFilter")) {
            return new RequestFilterNodeBuilder(context, (String) attributes.remove("filter"));
        } else if (name.equals("viewSuffix")) {
            return new ViewSuffixNodeBuilder(context, (String) attributes.remove("suffix"));
        } else if (name.equals("registerActions")) {
            return new RegisterActionsNodeBuilder(parentContainer, context);
        }
        return null;
    }
}
