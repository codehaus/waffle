/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar.pico;

import org.codehaus.waffle.registrar.Reference;
import org.codehaus.waffle.registrar.ComponentReference;
import org.codehaus.waffle.registrar.SessionAttributeReference;
import org.codehaus.waffle.registrar.RequestAttributeReference;
import org.codehaus.waffle.registrar.RequestParameterReference;
import org.codehaus.waffle.registrar.ServletContextAttributeReference;
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
