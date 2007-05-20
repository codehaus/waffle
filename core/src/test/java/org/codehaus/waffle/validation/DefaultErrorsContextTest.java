package org.codehaus.waffle.validation;

import junit.framework.TestCase;

public class DefaultErrorsContextTest extends TestCase {

    public void testEachCountMethod() {
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

    public void testHasErrors() {
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

    public void testHasBindErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext();
        assertFalse(context.hasBindErrorMessages("fieldName"));

        context.addErrorMessage(new BindErrorMessage("fieldName", null, null));
        assertTrue(context.hasBindErrorMessages("fieldName"));
    }

    public void testHasFieldErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext();
        assertFalse(context.hasFieldErrorMessages("fieldName"));

        context.addErrorMessage(new FieldErrorMessage("fieldName", null, null));
        assertTrue(context.hasFieldErrorMessages("fieldName"));
    }

    public void testHasGlobalErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext();
        assertFalse(context.hasGlobalErrorMessages());

        context.addErrorMessage(new GlobalErrorMessage("an error message"));
        assertTrue(context.hasGlobalErrorMessages());
    }

    public void testGetAllErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext();
        assertEquals(0, context.getAllErrorMessages().size());

        context.addErrorMessage(new BindErrorMessage(null, null, null));
        context.addErrorMessage(new FieldErrorMessage(null, null, null));
        context.addErrorMessage(new GlobalErrorMessage(null));

        assertEquals(3, context.getAllErrorMessages().size());
    }

    public void testGetAllBindErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext();
        assertEquals(0, context.getAllBindErrorMessages().size());

        context.addErrorMessage(new BindErrorMessage(null, null, null));
        assertEquals(1, context.getAllBindErrorMessages().size());
    }

    public void testGetAllFieldErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext();
        assertEquals(0, context.getAllFieldErrorMessages().size());

        context.addErrorMessage(new FieldErrorMessage(null, null, null));
        assertEquals(1, context.getAllFieldErrorMessages().size());
    }

    public void testGetAllGlobalErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext();
        assertEquals(0, context.getAllGlobalErrorMessages().size());

        context.addErrorMessage(new GlobalErrorMessage(null));
        assertEquals(1, context.getAllGlobalErrorMessages().size());
    }

    public void testGetBindErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext();
        assertEquals(0, context.getBindErrorMessages("fieldName").size());

        context.addErrorMessage(new BindErrorMessage("fieldName", null, null));
        assertEquals(1, context.getBindErrorMessages("fieldName").size());
    }
    
    public void testGetFieldErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext();
        assertEquals(0, context.getFieldErrorMessages("fieldName").size());

        context.addErrorMessage(new FieldErrorMessage("fieldName", null, null));
        assertEquals(1, context.getFieldErrorMessages("fieldName").size());
    }

}
