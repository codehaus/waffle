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

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoInitializationException;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.PicoVisitor;
import org.codehaus.waffle.context.CurrentHttpServletRequest;

/**
 * This class allows for components to be dependent on an HttpSession without actually adding the session to the
 * picocontainer.  Without this serilaization issue can occur when an application server tries to serialize a
 * users session (<a href="http://jira.codehaus.org/browse/WAFFLE-60">WAFFLE-60</a>.)  
 *
 * @author Michael Ward
 */
import javax.servlet.http.HttpSession;

public class HttpSessionComponentAdapter implements ComponentAdapter {
    private final Class componentImplementation = HttpSession.class;

    public Object getComponentKey() {
        return componentImplementation;
    }

    public Class getComponentImplementation() {
        return componentImplementation;
    }

    public Object getComponentInstance(PicoContainer container) throws PicoInitializationException, PicoIntrospectionException {
        return CurrentHttpServletRequest.get().getSession();
    }

    public void verify(PicoContainer container) throws PicoIntrospectionException {
        // do nothing
    }

    public void accept(PicoVisitor visitor) {
        // do nothing
    }
}
