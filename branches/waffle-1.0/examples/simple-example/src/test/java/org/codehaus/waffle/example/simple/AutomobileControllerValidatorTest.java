package org.codehaus.waffle.example.simple;

import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.validation.DefaultErrorsContext;
import org.codehaus.waffle.validation.ErrorMessage;
import org.codehaus.waffle.validation.ErrorsContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.List;

public class AutomobileControllerValidatorTest {

    @Test
    public void canValidateHasNoErrors() {
        MessageResources messageResources = new DefaultMessageResources();

        AutomobileController controller = new AutomobileController();
        controller.setSpeed(50);
        controller.setTopSpeed(150);

        AutomobileControllerValidator validator = new AutomobileControllerValidator(controller, messageResources);
        ErrorsContext errorsContext = new DefaultErrorsContext(null);
        validator.accelerate(errorsContext, 15);

        assertFalse(errorsContext.hasErrorMessages());
    }

    @Test
    public void canValidateHasErrors() {
        MessageResources messageResources = new DefaultMessageResources();

        AutomobileController controller = new AutomobileController();
        controller.setSpeed(140);
        controller.setTopSpeed(150);

        AutomobileControllerValidator validator = new AutomobileControllerValidator(controller, messageResources);
        ErrorsContext errorsContext = new DefaultErrorsContext(null);
        validator.accelerate(errorsContext, 15);

        assertTrue(errorsContext.hasErrorMessages());

        List<? extends ErrorMessage> fieldValidationMessages = errorsContext.getErrorMessagesForField(ErrorMessage.Type.FIELD, "speed");
        assertEquals(1, fieldValidationMessages.size());
        assertEquals("Speed can NOT exceed the top speed [150]", fieldValidationMessages.get(0).getMessage());
    }
}
