package org.codehaus.waffle.taglib;

import static org.junit.Assert.assertEquals;

import org.codehaus.waffle.validation.BindErrorMessage;
import org.codehaus.waffle.validation.DefaultErrorsContext;
import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.FieldErrorMessage;
import org.codehaus.waffle.validation.GlobalErrorMessage;
import org.junit.Test;

public class FunctionsTest {

    @Test
    public void canFindErrors() {
        ErrorsContext errorsContext = new DefaultErrorsContext();
        errorsContext.addErrorMessage(new BindErrorMessage("bind", "value", "message bind"));
        errorsContext.addErrorMessage(new FieldErrorMessage("field", "value", "message field"));
        errorsContext.addErrorMessage(new GlobalErrorMessage("global"));

        assertEquals(3, Functions.findAllErrors(errorsContext).size());
        assertEquals(1, Functions.findErrors(errorsContext, "BIND").size());
        assertEquals(1, Functions.findErrorsForField(errorsContext, "BIND", "bind").size());
        assertEquals(1, Functions.findErrors(errorsContext, "FIELD").size());
        assertEquals(1, Functions.findErrorsForField(errorsContext, "FIELD", "field").size());
        assertEquals(1, Functions.findErrors(errorsContext, "GLOBAL").size());
        assertEquals(1, Functions.findErrorsForField(errorsContext, "GLOBAL", "any").size());

        assertEquals("message bind", ((BindErrorMessage) Functions.findErrorsForField(errorsContext, "BIND", "bind").get(0)).getMessage());
        assertEquals("message field", ((FieldErrorMessage) Functions.findErrorsForField(errorsContext, "FIELD", "field").get(0)).getMessage());
    }

}
