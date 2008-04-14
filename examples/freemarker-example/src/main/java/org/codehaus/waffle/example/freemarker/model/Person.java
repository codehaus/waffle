package org.codehaus.waffle.example.freemarker.model;

import java.util.Date;
import java.util.List;

public interface Person {

    public Long getId();

    public String getFirstName();

    public String getLastName();

    public String getEmail();

    public Date getDateOfBirth();

    public List<String> getSkills();
    
    public List<Integer> getLevels();
    
    public List<Double> getGrades();
    
}
