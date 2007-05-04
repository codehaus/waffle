package com.thoughtworks.waffle.example.simple;

import com.thoughtworks.waffle.example.simple.MyRegistrar;
import com.thoughtworks.waffle.registrar.mock.AbstractPicoRegistrarTestCase;

public class MyRegistrarTest extends AbstractPicoRegistrarTestCase {

    public void testConfiguration() {
        assertConfiguration(MyRegistrar.class);
    }
}
