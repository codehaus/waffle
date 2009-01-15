/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar.pico;

import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.jmock.integration.junit4.JMock;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.picocontainer.Parameter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.behaviors.Caching;
import org.codehaus.waffle.testmodel.DependsOnValue;
import org.codehaus.waffle.bind.StringTransmuter;

import javax.servlet.http.HttpServletRequest;

@RunWith(JMock.class)
public class RequestParameterParameterTest {
    private Mockery mockery = new Mockery();

    @Test
    public void componentDependsOnRequestParameter() {
        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                exactly(2).of(request).getParameter("foobar");
                will(returnValue("helloWorld"));
                exactly(2).of(stringTransmuter).transmute("helloWorld", String.class);
                will(returnValue("helloWorld"));
            }
        });

        Parameter[] parameters = {new RequestParameterParameter("foobar", stringTransmuter, null)};

        MutablePicoContainer pico = new DefaultPicoContainer(new Caching());
        pico.addComponent(request);
        pico.addComponent("x", DependsOnValue.class, parameters);

        DependsOnValue dependsOnValue = (DependsOnValue) pico.getComponent("x");

        assertEquals("helloWorld", dependsOnValue.getValue());
    }

    @Test
    public void willReturnDefaultValueWhenTransmuterReturnsNull() {
        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                exactly(2).of(request).getParameter("foobar");
                will(returnValue(null));
                exactly(2).of(stringTransmuter).transmute(null, String.class);
                will(returnValue(null));
            }
        });

        Parameter[] parameters = {new RequestParameterParameter("foobar", stringTransmuter, "the default value")};

        MutablePicoContainer pico = new DefaultPicoContainer(new Caching());
        pico.addComponent(request);
        pico.addComponent("x", DependsOnValue.class, parameters);

        DependsOnValue dependsOnValue = (DependsOnValue) pico.getComponent("x");

        assertEquals("the default value", dependsOnValue.getValue());
    }
}
