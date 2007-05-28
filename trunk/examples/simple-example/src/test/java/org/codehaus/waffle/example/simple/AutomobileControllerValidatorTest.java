package org.codehaus.waffle.example.simple;

import org.codehaus.waffle.example.simple.AutomobileController;
import org.codehaus.waffle.example.simple.AutomobileControllerValidator;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.DefaultErrorsContext;
import org.codehaus.waffle.validation.FieldErrorMessage;
import junit.framework.TestCase;

import java.util.List;

public class AutomobileControllerValidatorTest extends TestCase {

    public void testValidateHasNoErrors() {
        MessageResources messageResources = new DefaultMessageResources();

        AutomobileController controller = new AutomobileController();
        controller.setSpeed(50);
        controller.setTopSpeed(150);

        AutomobileControllerValidator validator = new AutomobileControllerValidator(controller, messageResources);
        ErrorsContext errorsContext = new DefaultErrorsContext();
        validator.accelerate(errorsContext, 15);

        assertFalse(errorsContext.hasErrorMessages());
    }

    public void testValidateHasErrors() {
        MessageResources messageResources = new DefaultMessageResources();

        AutomobileController controller = new AutomobileController();
        controller.setSpeed(140);
        controller.setTopSpeed(150);

        AutomobileControllerValidator validator = new AutomobileControllerValidator(controller, messageResources);
        ErrorsContext errorsContext = new DefaultErrorsContext();
        validator.accelerate(errorsContext, 15);

        assertTrue(errorsContext.hasErrorMessages());

        List<FieldErrorMessage> fieldValidationMessages = errorsContext.getFieldErrorMessages("speed");
        assertEquals(1, fieldValidationMessages.size());
        assertEquals("Speed can NOT exceed the top speed [150]", fieldValidationMessages.get(0).getMessage());
    }
}