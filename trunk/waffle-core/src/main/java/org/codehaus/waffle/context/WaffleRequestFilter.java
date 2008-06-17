/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context;

import org.codehaus.waffle.ComponentRegistry;
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
        ComponentRegistry componentRegistry = ServletContextHelper
                .getComponentRegistry(filterConfig.getServletContext());

        contextContainerFactory = componentRegistry.getContextContainerFactory();
    }

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        CurrentHttpServletRequest.set(httpServletRequest);
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
