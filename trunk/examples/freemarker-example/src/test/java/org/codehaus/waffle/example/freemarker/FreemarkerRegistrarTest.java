package org.codehaus.waffle.example.freemarker;

import org.codehaus.waffle.mock.PicoRegistrarMockery;
import org.junit.Test;

public class FreemarkerRegistrarTest {

    @Test
    public void canAssertConfiguration() {
        new PicoRegistrarMockery().assertConfiguration(FreemarkerRegistrar.class);
    }

}
