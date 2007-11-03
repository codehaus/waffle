package org.codehaus.waffle.example.paranamer;

import org.codehaus.waffle.mock.PicoRegistrarMockery;
import org.codehaus.waffle.registrar.mock.AbstractPicoRegistrarTestCase;

public class ParanamerRegistrarTest extends AbstractPicoRegistrarTestCase {

    public void testConfiguration() {
        assertConfiguration(ParanamerRegistrar.class);
    }

    public void testConfigurationWithMockery() {
        PicoRegistrarMockery mockery = new PicoRegistrarMockery();
        mockery.assertConfiguration(ParanamerRegistrar.class);
    }

}
