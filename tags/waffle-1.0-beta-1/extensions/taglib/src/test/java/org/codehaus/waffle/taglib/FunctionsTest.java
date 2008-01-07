package org.codehaus.waffle.taglib;

import junit.framework.TestCase;
import org.codehaus.waffle.validation.DefaultErrorsContext;
import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.FieldErrorMessage;

import java.util.List;

public class FunctionsTest extends TestCase {

    public void testFindFieldErrors() {
        ErrorsContext errorsContext = new DefaultErrorsContext();
        errorsContext.addErrorMessage(new FieldErrorMessage("name", "value", "message"));

        List<FieldErrorMessage> messages = Functions.findFieldErrors(errorsContext, "name");
        assertEquals(1, messages.size());

        FieldErrorMessage fieldValidationMessage = messages.get(0);
        assertEquals("message", fieldValidationMessage.getMessage());
    }
}