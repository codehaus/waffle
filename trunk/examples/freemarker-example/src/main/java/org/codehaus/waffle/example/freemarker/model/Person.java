package org.codehaus.waffle.example.freemarker.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    
    double getMagicNumber();

    String getNotes();
    
    Map<String,List<Integer>> getNumberLists();

    Map<String,List<String>> getStringLists();

}
