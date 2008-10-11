package org.codehaus.waffle.mock;

public class ParameterController {
    private String name;
    private Integer age;

    public ParameterController(String name, Integer age) {
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
