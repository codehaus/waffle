package org.codehaus.waffle.example.simple;

public class AutomobileController {
    private String make = "ford";
    private String model = "mustang";
    private int speed = 0;
    private int topSpeed;

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

    public void stop() {
        speed = 0;
    }
}
