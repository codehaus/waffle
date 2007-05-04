package org.codehaus.waffle.example.paranamer;

import org.codehaus.waffle.example.paranamer.MyRegistrar;
import org.codehaus.waffle.registrar.mock.AbstractPicoRegistrarTestCase;

public class MyRegistrarTest extends AbstractPicoRegistrarTestCase {

    public void testConfiguration() {
        assertConfiguration(MyRegistrar.class);
    }
}
