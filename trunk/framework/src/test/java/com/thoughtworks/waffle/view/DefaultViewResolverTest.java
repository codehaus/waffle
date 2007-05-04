package com.thoughtworks.waffle.view;

import junit.framework.TestCase;

public class DefaultViewResolverTest extends TestCase {

    public void testResolve() {
        View view = new View("/helloWorld.jsp", null);
        ViewResolver viewResolver = new DefaultViewResolver();

        assertEquals("/helloWorld.jsp", viewResolver.resolve(view));
    }
}
