package org.codehaus.waffle.example.simple;

import org.codehaus.waffle.mock.PicoRegistrarMockery;
import org.junit.Test;

public class SimpleRegistrarTest {

    @Test
    public void canAssertConfiguration() {
        PicoRegistrarMockery mockery = new PicoRegistrarMockery();
        mockery.assertConfiguration(SimpleRegistrar.class);
    }

}
