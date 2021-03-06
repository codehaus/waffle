package org.codehaus.waffle.example.freemarker.persister;

import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.waffle.example.freemarker.model.Person;

public class PersistablePerson implements Person {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Date dateOfBirth;
    private Date birthDay;
    private Date birthTime;
    private List<String> skills;
    private List<Integer> levels;
    private List<Double> grades;
    private String notes;
    private boolean wizard;
    private double magicNumber;
    private Type type;
    private Person bestFriend;
    private List<Person> friends;
    private Map<String, List<Integer>> numberLists;
    private Map<String, List<String>> stringLists;

    public PersistablePerson() {
        id = new Long(0);
        firstName = "";
        lastName = "";
        email = "";
        dateOfBirth = new Date();
        birthDay = new Date();
        birthTime = new Date();
        bestFriend = this;
        friends = new ArrayList<Person>();
        skills = new ArrayList<String>();
        levels = new ArrayList<Integer>();
        grades = new ArrayList<Double>();
        notes = "";
        type = Type.APPRENTICE;
        wizard = false;
        magicNumber = 0.d;
        numberLists = new HashMap<String, List<Integer>>();
        stringLists = new HashMap<String, List<String>>();
    }

    public PersistablePerson(Person person) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
        this.dateOfBirth = person.getDateOfBirth();
        this.birthDay = person.getBirthDay();
        this.birthTime = person.getBirthTime();
        this.bestFriend = person.getBestFriend();
        this.friends = person.getFriends();
        this.skills = person.getSkills();
        this.levels = person.getLevels();
        this.grades = person.getGrades();
        this.notes = person.getNotes();
        this.type = person.getType();
        this.wizard = person.isWizard();
        this.magicNumber = person.getMagicNumber();
        this.numberLists = person.getNumberLists();
        this.stringLists = person.getStringLists();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Date getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(Date birthTime) {
        this.birthTime = birthTime;
    }

    public Person getBestFriend() {
        return bestFriend;
    }

    public void setBestFriend(Person bestFriend) {
        this.bestFriend = bestFriend;
    }

    public List<Person> getFriends() {
        return friends;
    }

    public void setFriends(List<Person> friends) {
        this.friends = friends;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<Integer> getLevels() {
        return levels;
    }

    public void setLevels(List<Integer> levels) {
        this.levels = levels;
    }

    public List<Double> getGrades() {
        return grades;
    }

    public void setGrades(List<Double> grades) {
        this.grades = grades;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isWizard() {
        return wizard;
    }

    public boolean getWizard() {
        return wizard;
    }

    public void setWizard(boolean wizard) {
        this.wizard = wizard;
    }

    public double getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(double magicNumber) {
        this.magicNumber = magicNumber;
    }

    public Map<String, List<Integer>> getNumberLists() {
        return numberLists;
    }

    public Map<String, List<String>> getStringLists() {
        return stringLists;
    }

    public void setNumberLists(Map<String, List<Integer>> numberLists) {
        this.numberLists = numberLists;
    }

    public void setStringLists(Map<String, List<String>> stringLists) {
        this.stringLists = stringLists;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);
    }

}
