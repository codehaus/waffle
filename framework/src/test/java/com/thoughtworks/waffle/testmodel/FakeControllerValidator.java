package com.thoughtworks.waffle.testmodel;

import com.thoughtworks.waffle.validation.ErrorsContext;

public class FakeControllerValidator {
    public ErrorsContext errorsContext;
    public String value;

    public void sayHello(ErrorsContext errorsContext, String value) {
        this.errorsContext = errorsContext;
        this.value = value;
    }
}
