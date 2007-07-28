package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.bind.BindErrorMessageResolver;

public class StubBindErrorMessageResolver implements BindErrorMessageResolver {

    public String resolve(Object model, String propertyName, String value) {
        throw new UnsupportedOperationException();
    }
}
