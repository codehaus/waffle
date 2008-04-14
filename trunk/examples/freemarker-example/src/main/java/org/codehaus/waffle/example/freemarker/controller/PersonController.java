package org.codehaus.waffle.example.freemarker.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.codehaus.waffle.example.freemarker.model.Person;
import org.codehaus.waffle.example.freemarker.persister.PersistablePerson;
import org.codehaus.waffle.example.freemarker.persister.PersonPersister;

public class PersonController {
    private final PersonPersister persister;
    private Person person;
    private List<String> selectedIds = new ArrayList<String>();
    private List<String> skills = Arrays.asList("Magician", "Apprentice");
    
    public PersonController(PersonPersister persister) {
        this.persister = persister;
    }

    public Collection<Person> getPeople() {
        return persister.findAll();
    }

    public List<String> getSelectedIds(){
        return selectedIds;
    }
    
    public void setSelectedIds(List<String> ids){
        selectedIds = ids;
    }

    public Collection<Person> getSelectedPeople() {
        List<Person> selected = new ArrayList<Person>();
        for ( String id : selectedIds ){
            selected.add(persister.findById(Long.parseLong(id)));
        }
        return selected;
    }
    
    public void remove(Long personId) {
        persister.delete(personId);
    }

    public void select(Long id) {
        this.person = persister.findById(id);
    }

    public void show() {
        //do nothing:  the selected Ids and people are automatically populated
    }
    
    public void save() {
        persister.save(person);
    }
    
    public void create() {
        person = new PersistablePerson();
    }

    public void cancel() {
        person = null;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<String> getSkills() {
        return skills;
    }
    
}
