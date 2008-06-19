package org.codehaus.waffle.example.freemarker.model;

import java.util.Date;
import java.util.List;

public interface Person {

    enum Type {
        WIZARD, APPRENTICE
    }
    
    Long getId();

    String getFirstName();

    String getLastName();

    String getEmail();

    Date getDateOfBirth();

    Date getBirthDay();
    
    Date getBirthTime();

    Person getBestFriend();

    List<Person> getFriends();
    
    List<String> getSkills();
    
    List<Integer> getLevels();
    
    List<Double> getGrades();

    Type getType(); 
    
    boolean isWizard();
    
    String getNotes();
    
}
