/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context;

import org.picocontainer.MutablePicoContainer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Implementors of this interface are responsible for maintaining the components defined in an applications Registrar.
 * This abstraction of the container relies on its ability to maintain trees of containers which allows Waffle to work
 * so well.
 * 
 * @author Michael Ward
 */
public interface ContextContainerFactory {

    void initialize(ServletContext servletContext);

    void destroy();

    MutablePicoContainer buildSessionLevelContainer();

    MutablePicoContainer buildRequestLevelContainer(HttpServletRequest request);

}
