package org.codehaus.waffle.example.paranamer;

import org.codehaus.waffle.mock.PicoRegistrarMockery;
import org.junit.Test;

public class ParanamerRegistrarTest {

    @Test
    public void canAssertConfiguration() {
        PicoRegistrarMockery mockery = new PicoRegistrarMockery();
        mockery.assertConfiguration(ParanamerRegistrar.class);
    }

}
