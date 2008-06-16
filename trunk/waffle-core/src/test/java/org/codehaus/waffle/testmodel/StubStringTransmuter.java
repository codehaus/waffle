package org.codehaus.waffle.testmodel;

import java.lang.reflect.Type;

import org.codehaus.waffle.bind.StringTransmuter;

public class StubStringTransmuter implements StringTransmuter{
    public Object transmute(String value, Type toType) {
        return null;
    }
}
