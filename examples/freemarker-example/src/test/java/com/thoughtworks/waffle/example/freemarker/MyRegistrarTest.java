package com.thoughtworks.waffle.example.freemarker;

import com.thoughtworks.waffle.example.freemarker.MyRegistrar;
import com.thoughtworks.waffle.registrar.mock.AbstractPicoRegistrarTestCase;

public class MyRegistrarTest extends AbstractPicoRegistrarTestCase {

    public void testConfiguration() {
        assertConfiguration(MyRegistrar.class);
    }
}
