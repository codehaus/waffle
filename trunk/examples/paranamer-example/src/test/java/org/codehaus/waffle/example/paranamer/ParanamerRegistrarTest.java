package org.codehaus.waffle.example.paranamer;

import org.codehaus.waffle.mock.PicoRegistrarMockery;
import org.junit.Test;

public class ParanamerRegistrarTest {

    @Test
    public void canAssertConfiguration() {
        new PicoRegistrarMockery().assertConfiguration(ParanamerRegistrar.class);
    }

}
