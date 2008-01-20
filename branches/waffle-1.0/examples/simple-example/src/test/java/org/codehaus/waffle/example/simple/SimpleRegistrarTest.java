package org.codehaus.waffle.example.simple;

import org.codehaus.waffle.mock.PicoRegistrarMockery;
import org.junit.Test;

public class SimpleRegistrarTest {

    @Test
    public void canAssertConfiguration() {
        new PicoRegistrarMockery().assertConfiguration(SimpleRegistrar.class);
    }

}
