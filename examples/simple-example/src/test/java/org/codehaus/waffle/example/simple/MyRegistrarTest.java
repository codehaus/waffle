package org.codehaus.waffle.example.simple;

import org.codehaus.waffle.example.simple.MyRegistrar;
import org.codehaus.waffle.registrar.mock.AbstractPicoRegistrarTestCase;

public class MyRegistrarTest extends AbstractPicoRegistrarTestCase {

    public void testConfiguration() {
        assertConfiguration(MyRegistrar.class);
    }
}
