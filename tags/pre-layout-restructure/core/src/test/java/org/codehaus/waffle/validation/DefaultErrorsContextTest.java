package org.codehaus.waffle.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * 
 * @author Michael Ward
 */
public class DefaultErrorsContextTest {

    @Test
    public void canGetEachCountMethod() {
        ErrorsContext context = new DefaultErrorsContext();
        context.addErrorMessage(new BindErrorMessage("bind.error", "foobar", null));
        context.addErrorMessage(new FieldErrorMessage("field.error", "foobaz", null));
        context.addErrorMessage(new GlobalErrorMessage("global message"));

        assertTrue(context.hasErrorMessages());
        assertEquals(3, context.getErrorMessageCount());
        assertEquals(1, context.getBindErrorMessageCount());
        assertEquals(1, context.getFieldErrorMessageCount());
        assertEquals(1, context.getGlobalErrorMessageCount());
    }

    @Test
    public void hasErrors() {
        ErrorsContext context = new DefaultErrorsContext();
        assertFalse(context.hasErrorMessages());

        // field errors
        context.addErrorMessage(new FieldErrorMessage(null, null, null));
        assertTrue(context.hasErrorMessages());

        // bind errors
        context = new DefaultErrorsContext();
        context.addErrorMessage(new BindErrorMessage(null, null, null));
        assertTrue(context.hasErrorMessages());

        // global errors
        context = new DefaultErrorsContext();
        context.addErrorMessage(new GlobalErrorMessage(null));
        assertTrue(context.hasErrorMessages());
    }

    @Test
    public void hasBindErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext();
        assertFalse(context.hasBindErrorMessages("fieldName"));

        context.addErrorMessage(new BindErrorMessage("fieldName", null, null));
        assertTrue(context.hasBindErrorMessages("fieldName"));
    }

    @Test
    public void hasFieldErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext();
        assertFalse(context.hasFieldErrorMessages("fieldName"));

        context.addErrorMessage(new FieldErrorMessage("fieldName", null, null));
        assertTrue(context.hasFieldErrorMessages("fieldName"));
    }

    @Test
    public void hasGlobalErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext();
        assertFalse(context.hasGlobalErrorMessages());

        context.addErrorMessage(new GlobalErrorMessage("an error message"));
        assertTrue(context.hasGlobalErrorMessages());
    }

    @Test
    public void canGetAllErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext();
        assertEquals(0, context.getAllErrorMessages().size());

        context.addErrorMessage(new BindErrorMessage(null, null, null));
        context.addErrorMessage(new FieldErrorMessage(null, null, null));
        context.addErrorMessage(new GlobalErrorMessage(null));

        assertEquals(3, context.getAllErrorMessages().size());
    }

    @Test
    public void canGetAllBindErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext();
        assertEquals(0, context.getAllBindErrorMessages().size());

        context.addErrorMessage(new BindErrorMessage(null, null, null));
        assertEquals(1, context.getAllBindErrorMessages().size());
    }

    @Test
    public void canGetAllFieldErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext();
        assertEquals(0, context.getAllFieldErrorMessages().size());

        context.addErrorMessage(new FieldErrorMessage(null, null, null));
        assertEquals(1, context.getAllFieldErrorMessages().size());
    }

    @Test
    public void canGetAllGlobalErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext();
        assertEquals(0, context.getAllGlobalErrorMessages().size());

        context.addErrorMessage(new GlobalErrorMessage(null));
        assertEquals(1, context.getAllGlobalErrorMessages().size());
    }

    @Test
    public void canGetBindErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext();
        assertEquals(0, context.getBindErrorMessages("fieldName").size());

        context.addErrorMessage(new BindErrorMessage("fieldName", null, null));
        assertEquals(1, context.getBindErrorMessages("fieldName").size());
    }

    @Test
    public void canGetFieldErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext();
        assertEquals(0, context.getFieldErrorMessages("fieldName").size());

        context.addErrorMessage(new FieldErrorMessage("fieldName", null, null));
        assertEquals(1, context.getFieldErrorMessages("fieldName").size());
    }

}
