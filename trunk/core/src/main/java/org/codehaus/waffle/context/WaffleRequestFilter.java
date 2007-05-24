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
package org.codehaus.waffle.context;

import org.codehaus.waffle.WaffleComponentRegistry;
import org.codehaus.waffle.servlet.ServletContextHelper;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class WaffleRequestFilter implements Filter {
    private ContextContainerFactory contextContainerFactory;

    public void init(FilterConfig filterConfig) throws ServletException {
        WaffleComponentRegistry componentRegistry = ServletContextHelper
                .getWaffleComponentRegistry(filterConfig.getServletContext());

        contextContainerFactory = componentRegistry.getContextContainerFactory();
    }

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        ContextContainer requestContextContainer = contextContainerFactory
                .buildRequestLevelContainer(httpServletRequest);

        try {
            RequestLevelContainer.set(requestContextContainer);
            requestContextContainer.registerComponentInstance(request);
            requestContextContainer.registerComponentInstance(response);
            requestContextContainer.start();

            filterChain.doFilter(request, response);
        } finally {
            requestContextContainer.stop();
            requestContextContainer.dispose();
            RequestLevelContainer.set(null);
        }
    }

    public void destroy() {
        contextContainerFactory = null;
    }
    
}