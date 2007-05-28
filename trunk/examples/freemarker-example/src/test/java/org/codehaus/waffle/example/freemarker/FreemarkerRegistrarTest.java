package org.codehaus.waffle.example.freemarker;

import org.codehaus.waffle.example.freemarker.FreemarkerRegistrar;
import org.codehaus.waffle.registrar.mock.AbstractPicoRegistrarTestCase;

public class FreemarkerRegistrarTest extends AbstractPicoRegistrarTestCase {

    public void testConfiguration() {
        assertConfiguration(FreemarkerRegistrar.class);
    }
}
