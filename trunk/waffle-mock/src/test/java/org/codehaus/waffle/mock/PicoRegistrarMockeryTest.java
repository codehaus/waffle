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
package org.codehaus.waffle.mock;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Expectations;
import org.junit.Test;

/**
 * @author Mauro Talevi
 */
public class PicoRegistrarMockeryTest {

    @Test
    public void canAssertConfigurationWithParameters() {
        PicoRegistrarMockery mockery = new PicoRegistrarMockery();
        final HttpServletRequest request = mockery.mockHttpServletRequest();
        mockery.checking(new Expectations(){{
            atLeast(1).of(request).getParameter(with(equal("age")));
            will(returnValue("30"));
        }});
        mockery.assertConfiguration(ParameterRegistrar.class);
    }

}
