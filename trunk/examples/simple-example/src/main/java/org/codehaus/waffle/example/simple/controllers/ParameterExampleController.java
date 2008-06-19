package org.codehaus.waffle.example.simple.controllers;

public class ParameterExampleController {
    private String name;
    private Integer age;

    public ParameterExampleController(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}
