package org.codehaus.waffle.example.freemarker.controller;

import org.codehaus.waffle.example.freemarker.model.Person;
import org.codehaus.waffle.example.freemarker.persister.PersonDAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PersonController {
    private final PersonDAO personDAO;
    private Person person;
    private List<String> selectedIds = new ArrayList<String>();
    
    public PersonController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    public Collection<Person> getPeople() {
        return personDAO.findAll();
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
            selected.add(personDAO.findById(Long.parseLong(id)));
        }
        return selected;
    }
    
    public void remove(Long personId) {
        personDAO.delete(personId);
    }

    public void select(Long id) {
        this.person = personDAO.findById(id);
    }

    public void show() {
        //do nothing:  the selected Ids and people are automatically populated
    }
    
    public void save() {
        personDAO.save(person);
    }
    
    public void create() {
        person = new Person();
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

}
