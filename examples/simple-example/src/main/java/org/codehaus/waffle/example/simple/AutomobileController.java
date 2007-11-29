package org.codehaus.waffle.example.simple;

import org.codehaus.waffle.i18n.DefaultMessagesContext;
import org.codehaus.waffle.i18n.MessagesContext;
import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.FieldErrorMessage;

public class AutomobileController {
    private String make = "ford";
    private String model = "mustang";
    private int speed = 0;
    private int topSpeed;

    private MessagesContext messages;
    
    public AutomobileController(){
        this(new DefaultMessagesContext());              
    }
    
    public AutomobileController(MessagesContext messages) {
        this.messages = messages;
    }

    public void init(String make, String model) {
        this.make = make;
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(int topSpeed) {
        this.topSpeed = topSpeed;
        messages.clearMessages();
        this.messages.addMessage("success", "Set top speed "+topSpeed);
    }

    public void accelerate(int value) {
        speed += value;
        messages.clearMessages();
        this.messages.addMessage("success", "Accellerated to speed "+speed);            
    }

    public void accelerate(ErrorsContext errorsContext, int value) {
        if(speed + value > topSpeed) {
            String message = "Speed cannot exceed top speed: "+topSpeed;
            errorsContext.addErrorMessage(new FieldErrorMessage("speed", "" + speed, message));
            messages.clearMessages();
        }
    }
    
    public void stop() {
        speed = 0;
    }

    public MessagesContext getMessages() {
        return messages;
    }
    
}
