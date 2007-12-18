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
package org.codehaus.waffle.registrar.pico;

import org.codehaus.waffle.registrar.Reference;
import org.codehaus.waffle.registrar.ComponentReference;
import org.codehaus.waffle.registrar.HttpSessionAttributeReference;
import org.codehaus.waffle.registrar.RequestAttributeReference;
import org.codehaus.waffle.registrar.RequestParameterReference;
import org.codehaus.waffle.registrar.ServletContextAttributeReference;
import org.junit.Assert;
import org.junit.Test;
import org.picocontainer.defaults.ComponentParameter;
import org.picocontainer.defaults.ConstantParameter;

public class DefaultPicoContainerParameterResolverTest {

    @Test
    public void canResolveConstantParameters() {
        PicoContainerParameterResolver parameterResolver = new DefaultPicoContainerParameterResolver();
        Assert.assertTrue(parameterResolver.resolve("foobar") instanceof ConstantParameter);
    }

    @Test
    public void canResolveComponentParameters() {
        PicoContainerParameterResolver parameterResolver = new DefaultPicoContainerParameterResolver();
        Reference reference = new ComponentReference("foo");

        Assert.assertTrue(parameterResolver.resolve(reference) instanceof ComponentParameter);
    }

    @Test
    public void canResolveRequestParameterParameter() {
        PicoContainerParameterResolver parameterResolver = new DefaultPicoContainerParameterResolver();
        Reference reference = new RequestParameterReference("foo");

        Assert.assertTrue(parameterResolver.resolve(reference) instanceof RequestParameterParameter);
    }
    
    @Test
    public void canResolveRequestAttributeParameter() {
        PicoContainerParameterResolver parameterResolver = new DefaultPicoContainerParameterResolver();
        Reference reference = new RequestAttributeReference("foo");

        Assert.assertTrue(parameterResolver.resolve(reference) instanceof RequestAttributeParameter);
    }

    @Test
    public void canResolveHttpSessionAttributeParameter() {
        PicoContainerParameterResolver parameterResolver = new DefaultPicoContainerParameterResolver();
        Reference reference = new HttpSessionAttributeReference("foo");

        Assert.assertTrue(parameterResolver.resolve(reference) instanceof HttpSessionAttributeParameter);
    }

    @Test
    public void canResolveServletContextAttributeParameter() {
        PicoContainerParameterResolver parameterResolver = new DefaultPicoContainerParameterResolver();
        Reference reference = new ServletContextAttributeReference("foo");

        Assert.assertTrue(parameterResolver.resolve(reference) instanceof ServletContextAttributeParameter);
    }
}
