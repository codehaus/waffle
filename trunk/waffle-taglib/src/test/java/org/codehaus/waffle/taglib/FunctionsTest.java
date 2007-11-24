package org.codehaus.waffle.taglib;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.codehaus.waffle.validation.DefaultErrorsContext;
import org.codehaus.waffle.validation.ErrorMessage;
import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.FieldErrorMessage;
import org.junit.Test;

public class FunctionsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void canFindFieldErrors() {
        ErrorsContext errorsContext = new DefaultErrorsContext();
        errorsContext.addErrorMessage(new FieldErrorMessage("name", "value", "message"));

        List<? extends ErrorMessage> messages = Functions.findFieldErrors(errorsContext, "name");
        assertEquals(1, messages.size());

        FieldErrorMessage fieldValidationMessage = (FieldErrorMessage) messages.get(0);
        assertEquals("message", fieldValidationMessage.getMessage());
    }
}
