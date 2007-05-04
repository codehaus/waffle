package com.thoughtworks.waffle.testmodel;

import com.thoughtworks.waffle.bind.BindErrorMessageResolver;

public class StubBindErrorMessageResolver implements BindErrorMessageResolver {

    public String resolve(Object model, String propertyName, String value) {
        throw new UnsupportedOperationException();
    }
}
