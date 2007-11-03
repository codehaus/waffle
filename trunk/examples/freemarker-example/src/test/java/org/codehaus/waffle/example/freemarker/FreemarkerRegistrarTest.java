package org.codehaus.waffle.example.freemarker;

import org.codehaus.waffle.mock.PicoRegistrarMockery;
import org.codehaus.waffle.registrar.mock.AbstractPicoRegistrarTestCase;

public class FreemarkerRegistrarTest extends AbstractPicoRegistrarTestCase {

    public void testConfiguration() {
        assertConfiguration(FreemarkerRegistrar.class);
    }

    public void testConfigurationWithMockery() {
        PicoRegistrarMockery mockery = new PicoRegistrarMockery();
        mockery.assertConfiguration(FreemarkerRegistrar.class);
    }

}
