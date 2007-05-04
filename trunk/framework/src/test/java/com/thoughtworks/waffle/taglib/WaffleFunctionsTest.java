package com.thoughtworks.waffle.taglib;

import junit.framework.TestCase;
import com.thoughtworks.waffle.validation.ErrorsContext;
import com.thoughtworks.waffle.validation.FieldErrorMessage;
import com.thoughtworks.waffle.validation.DefaultErrorsContext;

import java.util.List;

public class WaffleFunctionsTest extends TestCase {

    public void testFindFieldErrors() {
        ErrorsContext errorsContext = new DefaultErrorsContext();
        errorsContext.addErrorMessage(new FieldErrorMessage("name", "value", "message"));

        List messages = WaffleFunctions.findFieldErrors(errorsContext, "name");
        assertEquals(1, messages.size());

        FieldErrorMessage fieldValidationMessage = (FieldErrorMessage) messages.get(0);
        assertEquals("message", fieldValidationMessage.getMessage());
    }
}
