package org.codehaus.waffle.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DefaultViewResolverTest {

    @Test
    public void canResolve() {
        String path = "/helloWorld.jsp";
        View view = new View(path);
        ViewResolver viewResolver = new DefaultViewResolver();
        assertEquals(path, viewResolver.resolve(view));
    }
}
