/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.pico;

import org.codehaus.waffle.context.Reference;
import org.codehaus.waffle.context.ComponentReference;
import org.codehaus.waffle.pico.ParameterResolver;
import org.codehaus.waffle.pico.RequestParameterParameter;
import org.codehaus.waffle.pico.SessionAttributeParameter;
import org.codehaus.waffle.context.SessionAttributeReference;
import org.codehaus.waffle.context.RequestAttributeReference;
import org.codehaus.waffle.context.RequestParameterReference;
import org.codehaus.waffle.context.ServletContextAttributeReference;
import org.codehaus.waffle.pico.RequestAttributeParameter;
import org.codehaus.waffle.pico.ServletContextAttributeParameter;
import org.junit.Assert;
import org.junit.Test;
import org.picocontainer.parameters.ConstantParameter;
import org.picocontainer.parameters.ComponentParameter;

public class DefaultPicoContainerParameterResolverTest {

    @Test
    public void canResolveConstantParameters() {
        ParameterResolver parameterResolver = new ParameterResolver(null);
        Assert.assertTrue(parameterResolver.resolve("foobar") instanceof ConstantParameter);
    }

    @Test
    public void canResolveComponentParameters() {
        ParameterResolver parameterResolver = new ParameterResolver(null);
        Reference reference = new ComponentReference("foo");

        Assert.assertTrue(parameterResolver.resolve(reference) instanceof ComponentParameter);
    }

    @Test
    public void canResolveRequestParameterParameter() {
        ParameterResolver parameterResolver = new ParameterResolver(null);
        Reference reference = new RequestParameterReference("foo");

        Assert.assertTrue(parameterResolver.resolve(reference) instanceof RequestParameterParameter);
    }
    
    @Test
    public void canResolveRequestAttributeParameter() {
        ParameterResolver parameterResolver = new ParameterResolver(null);
        Reference reference = new RequestAttributeReference("foo");

        Assert.assertTrue(parameterResolver.resolve(reference) instanceof RequestAttributeParameter);
    }

    @Test
    public void canResolveSessionAttributeParameter() {
        ParameterResolver parameterResolver = new ParameterResolver(null);
        Reference reference = new SessionAttributeReference("foo");

        Assert.assertTrue(parameterResolver.resolve(reference) instanceof SessionAttributeParameter);
    }

    @Test
    public void canResolveServletContextAttributeParameter() {
        ParameterResolver parameterResolver = new ParameterResolver(null);
        Reference reference = new ServletContextAttributeReference("foo");

        Assert.assertTrue(parameterResolver.resolve(reference) instanceof ServletContextAttributeParameter);
    }
}
