package org.codehaus.waffle.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.codehaus.waffle.validation.ErrorMessage.Type.BIND;
import static org.codehaus.waffle.validation.ErrorMessage.Type.FIELD;
import static org.codehaus.waffle.validation.ErrorMessage.Type.GLOBAL;

import org.junit.Test;

/**
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class DefaultErrorsContextTest {

    @Test
    public void canCountMessage() {
        ErrorsContext context = new DefaultErrorsContext(null);
        context.addErrorMessage(new BindErrorMessage("bind.error", "foobar", null));
        context.addErrorMessage(new FieldErrorMessage("field.error", "foobaz", null));
        context.addErrorMessage(new GlobalErrorMessage("global message"));

        assertEquals(3, context.getErrorMessageCount());
        assertEquals(1, context.getErrorMessageCountOfType(BIND));
        assertEquals(1, context.getErrorMessageCountForField(BIND, "bind.error"));
        assertEquals(1, context.getErrorMessageCountOfType(FIELD));
        assertEquals(1, context.getErrorMessageCountForField(FIELD, "field.error"));
        assertEquals(1, context.getErrorMessageCountOfType(GLOBAL));
        assertEquals(1, context.getErrorMessageCountForField(GLOBAL, "any.field.name"));
    }

    @Test
    public void canDetermineIfContextHasMessagesByType() {
        ErrorsContext context = new DefaultErrorsContext(null);
        assertFalse(context.hasErrorMessages());

        // bind errors
        assertFalse(context.hasErrorMessagesOfType(BIND));
        context.addErrorMessage(new BindErrorMessage(null, null, null));
        assertTrue(context.hasErrorMessagesOfType(BIND));

        // field errors
        assertFalse(context.hasErrorMessagesOfType(FIELD));
        context.addErrorMessage(new FieldErrorMessage(null, null, null));
        assertTrue(context.hasErrorMessagesOfType(FIELD));

        // global errors
        context = new DefaultErrorsContext(null);
        assertFalse(context.hasErrorMessagesOfType(GLOBAL));
        context.addErrorMessage(new GlobalErrorMessage(null));
        assertTrue(context.hasErrorMessagesOfType(GLOBAL));
        assertTrue(context.hasErrorMessages());
    }
    
    @Test
    public void canGetAllErrorMessages() {
        ErrorsContext context = new DefaultErrorsContext(null);
        assertEquals(0, context.getErrorMessagesOfType(BIND).size());
        assertEquals(0, context.getErrorMessagesOfType(FIELD).size());
        assertEquals(0, context.getErrorMessagesOfType(GLOBAL).size());

        context.addErrorMessage(new BindErrorMessage(null, null, null));
        context.addErrorMessage(new FieldErrorMessage(null, null, null));
        context.addErrorMessage(new GlobalErrorMessage(null));

        assertEquals(3, context.getAllErrorMessages().size());
        assertEquals(1, context.getErrorMessagesOfType(BIND).size());
        assertEquals(1, context.getErrorMessagesOfType(FIELD).size());
        assertEquals(1, context.getErrorMessagesOfType(GLOBAL).size());
    }

    @Test
    public void canGetErrorMessagesForField() {
        ErrorsContext context = new DefaultErrorsContext(null);
        assertFalse(context.hasErrorMessagesForField(BIND, "fieldName"));
        assertEquals(0, context.getErrorMessagesForField(BIND, "fieldName").size());
        assertFalse(context.hasErrorMessagesForField(FIELD, "fieldName"));
        assertEquals(0, context.getErrorMessagesForField(FIELD, "fieldName").size());

        context.addErrorMessage(new BindErrorMessage("fieldName", null, null));
        assertTrue(context.hasErrorMessagesForField(BIND, "fieldName"));
        assertEquals(1, context.getErrorMessagesForField(BIND, "fieldName").size());
        context.addErrorMessage(new FieldErrorMessage("fieldName", null, null));
        assertTrue(context.hasErrorMessagesForField(FIELD, "fieldName"));
        assertEquals(1, context.getErrorMessagesForField(FIELD, "fieldName").size());

    }

}
