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
package org.codehaus.waffle.registrar.pico;

import org.codehaus.waffle.registrar.Reference;
import org.codehaus.waffle.registrar.ComponentReference;
import org.codehaus.waffle.registrar.SessionAttributeReference;
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
        ParameterResolver parameterResolver = new DefaultParameterResolver(null);
        Assert.assertTrue(parameterResolver.resolve("foobar") instanceof ConstantParameter);
    }

    @Test
    public void canResolveComponentParameters() {
        ParameterResolver parameterResolver = new DefaultParameterResolver(null);
        Reference reference = new ComponentReference("foo");

        Assert.assertTrue(parameterResolver.resolve(reference) instanceof ComponentParameter);
    }

    @Test
    public void canResolveRequestParameterParameter() {
        ParameterResolver parameterResolver = new DefaultParameterResolver(null);
        Reference reference = new RequestParameterReference("foo");

        Assert.assertTrue(parameterResolver.resolve(reference) instanceof RequestParameterParameter);
    }
    
    @Test
    public void canResolveRequestAttributeParameter() {
        ParameterResolver parameterResolver = new DefaultParameterResolver(null);
        Reference reference = new RequestAttributeReference("foo");

        Assert.assertTrue(parameterResolver.resolve(reference) instanceof RequestAttributeParameter);
    }

    @Test
    public void canResolveSessionAttributeParameter() {
        ParameterResolver parameterResolver = new DefaultParameterResolver(null);
        Reference reference = new SessionAttributeReference("foo");

        Assert.assertTrue(parameterResolver.resolve(reference) instanceof SessionAttributeParameter);
    }

    @Test
    public void canResolveServletContextAttributeParameter() {
        ParameterResolver parameterResolver = new DefaultParameterResolver(null);
        Reference reference = new ServletContextAttributeReference("foo");

        Assert.assertTrue(parameterResolver.resolve(reference) instanceof ServletContextAttributeParameter);
    }
}
