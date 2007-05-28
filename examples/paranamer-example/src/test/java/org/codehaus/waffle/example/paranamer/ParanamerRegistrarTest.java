package org.codehaus.waffle.example.paranamer;

import org.codehaus.waffle.example.paranamer.ParanamerRegistrar;
import org.codehaus.waffle.registrar.mock.AbstractPicoRegistrarTestCase;

public class ParanamerRegistrarTest extends AbstractPicoRegistrarTestCase {

    public void testConfiguration() {
        assertConfiguration(ParanamerRegistrar.class);
    }
}
