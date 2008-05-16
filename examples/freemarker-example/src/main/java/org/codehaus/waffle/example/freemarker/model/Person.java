package org.codehaus.waffle.example.freemarker.model;

import java.util.Date;
import java.util.List;

public interface Person {

    public Long getId();

    public String getFirstName();

    public String getLastName();

    public String getEmail();

    public Date getDateOfBirth();

    public Date getBirthDay();
    
    public Date getBirthTime();
    
    public List<String> getSkills();
    
    public List<Integer> getLevels();
    
    public List<Double> getGrades();
    
    public boolean isWizard();
    
    public String getNotes();
    
}
