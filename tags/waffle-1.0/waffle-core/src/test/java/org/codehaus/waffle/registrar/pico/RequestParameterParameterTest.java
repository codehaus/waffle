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

import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.jmock.integration.junit4.JMock;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.picocontainer.Parameter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
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

        MutablePicoContainer pico = new DefaultPicoContainer();
        pico.registerComponentInstance(request);
        pico.registerComponentImplementation("x", DependsOnValue.class, parameters);

        DependsOnValue dependsOnValue = (DependsOnValue) pico.getComponentInstance("x");

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

        MutablePicoContainer pico = new DefaultPicoContainer();
        pico.registerComponentInstance(request);
        pico.registerComponentImplementation("x", DependsOnValue.class, parameters);

        DependsOnValue dependsOnValue = (DependsOnValue) pico.getComponentInstance("x");

        assertEquals("the default value", dependsOnValue.getValue());
    }
}
