package org.codehaus.waffle.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DefaultViewResolverTest {

    @Test
    public void canResolve() {
        View view = new View("/helloWorld.jsp", null);
        ViewResolver viewResolver = new DefaultViewResolver();

        assertEquals("/helloWorld.jsp", viewResolver.resolve(view));
    }
}
