package org.codehaus.waffle.example.simple;

import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.validation.FieldErrorMessage;
import org.codehaus.waffle.validation.ErrorsContext;

public class AutomobileControllerValidator {
    private final AutomobileController automobileController;
    private final MessageResources messageResources;

    public AutomobileControllerValidator(AutomobileController automobileController, MessageResources messageResources) {
        this.automobileController = automobileController;
        this.messageResources = messageResources;
    }

    public void accelerate(ErrorsContext errorsContext, int value) {
        int speed = automobileController.getSpeed() + value;
        int topSpeed = automobileController.getTopSpeed();

        if(speed > topSpeed) {
            String message = messageResources.getMessage("speed.error", topSpeed);
            errorsContext.addErrorMessage(new FieldErrorMessage("speed", "" + speed, message));
        }
    }

}
