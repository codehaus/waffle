package org.codehaus.waffle.example.simple;

import org.codehaus.waffle.example.simple.SimpleRegistrar;
import org.codehaus.waffle.registrar.mock.AbstractPicoRegistrarTestCase;

public class SimpleRegistrarTest extends AbstractPicoRegistrarTestCase {

    public void testConfiguration() {
        assertConfiguration(SimpleRegistrar.class);
    }
}
