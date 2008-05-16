package org.codehaus.waffle.example.freemarker.persister;

import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private Type type;

    public PersistablePerson() {
        id = new Long(0);
        firstName = "";
        lastName = "";
        email = "";
        dateOfBirth = new Date();
        birthDay = new Date();
        birthTime = new Date();
        skills = new ArrayList<String>();
        levels = new ArrayList<Integer>();
        grades = new ArrayList<Double>();
        notes = "";
        type = Type.APPRENTICE;
        wizard = false;
    }

    public PersistablePerson(Person person) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
        this.dateOfBirth = person.getDateOfBirth();
        this.birthDay = person.getBirthDay();
        this.birthTime = person.getBirthTime();
        this.skills = person.getSkills();
        this.levels = person.getLevels();
        this.grades = person.getGrades();
        this.notes = person.getNotes();
        this.type = person.getType();
        this.wizard = person.isWizard();
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);
    }
    
}
