package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.bind.StringTransmuter;

public class StubStringTransmuter implements StringTransmuter{
    public <T> T transmute(String value, Class<T> toType) {
        return null;
    }
}
