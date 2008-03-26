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

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.codehaus.waffle.context.CurrentHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Michael Ward
 */
@RunWith(JMock.class)
public class HttpSessionComponentAdapterTest {
    private Mockery mockery = new Mockery();

    @Test
    public void doIt() {
        HttpSessionComponentAdapter componentAdapter = new HttpSessionComponentAdapter();

        // Mock ServletContext
        final HttpServletRequest httpServletRequest = mockery.mock(HttpServletRequest.class);
        final HttpSession httpSession = mockery.mock(HttpSession.class);
        CurrentHttpServletRequest.set(httpServletRequest);
        
        mockery.checking(new Expectations() {
            {
                one(httpServletRequest).getSession();
                will(returnValue(httpSession));
            }
        });

        assertSame(httpSession, componentAdapter.getComponentInstance(null));
    }
}
