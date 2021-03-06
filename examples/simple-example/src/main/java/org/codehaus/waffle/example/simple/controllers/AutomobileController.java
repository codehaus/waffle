package org.codehaus.waffle.example.simple.controllers;

import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.FieldErrorMessage;
import org.codehaus.waffle.i18n.MessagesContext;

public class AutomobileController {
    private String make = "ford";
    private String model = "mustang";
    private int speed = 0;
    private int topSpeed;

    public AutomobileController(){
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
    }

    public void accelerate(int value) {
        speed += value;
    }

    public void accelerate(ErrorsContext errorsContext, int value) {
        if(speed + value > topSpeed) {
            String message = "Speed cannot exceed top speed: "+topSpeed;
            errorsContext.addErrorMessage(new FieldErrorMessage("speed", "" + speed, message));
        }
    }
    
    public void stop(MessagesContext messagesContext) {
        messagesContext.addMessage("stop", "Stop called");
        speed = 0;
    }
    
}
