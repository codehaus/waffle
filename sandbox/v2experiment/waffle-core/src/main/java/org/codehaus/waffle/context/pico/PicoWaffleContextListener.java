/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context.pico;

import javax.servlet.ServletContext;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.context.WaffleContextListener;

/**
 * Pico-based WaffleContextListener that uses PicoComponentRegistry instances.
 * 
 * @author Mauro Talevi
 */
public class PicoWaffleContextListener extends WaffleContextListener {

    protected ComponentRegistry buildComponentRegistry(ServletContext servletContext) {
        return new ComponentRegistry(servletContext);
    }

}
