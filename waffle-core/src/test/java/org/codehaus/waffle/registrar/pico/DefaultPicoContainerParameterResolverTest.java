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

import org.codehaus.waffle.registrar.Argument;
import org.codehaus.waffle.registrar.ComponentArgument;
import org.codehaus.waffle.registrar.HttpSessionAttributeArgument;
import org.codehaus.waffle.registrar.RequestAttributeArgument;
import org.codehaus.waffle.registrar.RequestParameterArgument;
import org.codehaus.waffle.registrar.ServletContextAttributeArgument;
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
        Argument argument = new ComponentArgument("foo");

        Assert.assertTrue(parameterResolver.resolve(argument) instanceof ComponentParameter);
    }

    @Test
    public void canResolveRequestParameterParameter() {
        PicoContainerParameterResolver parameterResolver = new DefaultPicoContainerParameterResolver();
        Argument argument = new RequestParameterArgument("foo");

        Assert.assertTrue(parameterResolver.resolve(argument) instanceof RequestParameterParameter);
    }
    
    @Test
    public void canResolveRequestAttributeParameter() {
        PicoContainerParameterResolver parameterResolver = new DefaultPicoContainerParameterResolver();
        Argument argument = new RequestAttributeArgument("foo");

        Assert.assertTrue(parameterResolver.resolve(argument) instanceof RequestAttributeParameter);
    }

    @Test
    public void canResolveHttpSessionAttributeParameter() {
        PicoContainerParameterResolver parameterResolver = new DefaultPicoContainerParameterResolver();
        Argument argument = new HttpSessionAttributeArgument("foo");

        Assert.assertTrue(parameterResolver.resolve(argument) instanceof HttpSessionAttributeParameter);
    }

    @Test
    public void canResolveServletContextAttributeParameter() {
        PicoContainerParameterResolver parameterResolver = new DefaultPicoContainerParameterResolver();
        Argument argument = new ServletContextAttributeArgument("foo");

        Assert.assertTrue(parameterResolver.resolve(argument) instanceof ServletContextAttributeParameter);   
    }
}
