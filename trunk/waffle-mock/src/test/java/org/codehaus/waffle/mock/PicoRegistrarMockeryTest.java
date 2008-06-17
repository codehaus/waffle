/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
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
