/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context.pico;

import org.codehaus.waffle.context.CurrentHttpServletRequest;
import org.codehaus.waffle.context.HttpSessionComponentAdapter;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    public void shouldReturnProxyToHttpSession() {
        HttpSessionComponentAdapter componentAdapter = new HttpSessionComponentAdapter();

        // Mock ServletContext
        final HttpServletRequest httpServletRequest = mockery.mock(HttpServletRequest.class);
        CurrentHttpServletRequest.set(httpServletRequest);
        Assert.assertTrue(componentAdapter.getComponentInstance(null) instanceof HttpSession);
    }
}
