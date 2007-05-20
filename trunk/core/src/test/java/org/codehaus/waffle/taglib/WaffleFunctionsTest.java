package org.codehaus.waffle.taglib;

import junit.framework.TestCase;
import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.FieldErrorMessage;
import org.codehaus.waffle.validation.DefaultErrorsContext;

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
