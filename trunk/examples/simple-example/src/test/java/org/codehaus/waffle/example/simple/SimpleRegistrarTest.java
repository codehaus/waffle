package org.codehaus.waffle.example.simple;

import org.codehaus.waffle.mock.PicoRegistrarMockery;
import org.codehaus.waffle.registrar.mock.AbstractPicoRegistrarTestCase;

public class SimpleRegistrarTest extends AbstractPicoRegistrarTestCase {

    public void testConfiguration() {
        assertConfiguration(SimpleRegistrar.class);
    }

    public void testConfigurationWithMockery() {
        PicoRegistrarMockery mockery = new PicoRegistrarMockery();
        mockery.assertConfiguration(SimpleRegistrar.class);
    }

}
