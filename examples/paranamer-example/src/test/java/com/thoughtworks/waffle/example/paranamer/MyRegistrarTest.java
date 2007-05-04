package com.thoughtworks.waffle.example.paranamer;

import com.thoughtworks.waffle.example.paranamer.MyRegistrar;
import com.thoughtworks.waffle.registrar.mock.AbstractPicoRegistrarTestCase;

public class MyRegistrarTest extends AbstractPicoRegistrarTestCase {

    public void testConfiguration() {
        assertConfiguration(MyRegistrar.class);
    }
}
