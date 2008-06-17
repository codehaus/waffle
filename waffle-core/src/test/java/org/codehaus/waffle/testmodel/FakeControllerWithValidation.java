/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.validation.ErrorsContext;

public class FakeControllerWithValidation extends FakeController {
   
    public ErrorsContext errorsContext;
    public String value;

    public void sayHello(ErrorsContext errorsContext, String value) {
        this.errorsContext = errorsContext;
        this.value = value;
    }

}
