package org.codehaus.waffle.example.freemarker.controller;

import static java.util.Arrays.asList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.codehaus.waffle.action.annotation.ActionMethod;
import org.codehaus.waffle.example.freemarker.model.Person;
import org.codehaus.waffle.example.freemarker.model.Person.Type;
import org.codehaus.waffle.example.freemarker.persister.PersistablePerson;
import org.codehaus.waffle.example.freemarker.persister.PersonPersister;
import org.codehaus.waffle.view.ExportView;
import org.codehaus.waffle.view.View;

@SuppressWarnings("serial")
public class PersonController implements Serializable {
    private final PersonPersister persister;
    private final DateProvider dateProvider;
    private Person person;
    private List<Long> selectedIds = new ArrayList<Long>();
    private List<String> skills = Arrays.asList("Magician", "Apprentice");
    private Long id;

    public PersonController(PersonPersister persister, DateProvider dateProvider) {
        this.persister = persister;
        this.dateProvider = dateProvider;
    }

    public DateProvider getDateProvider() {
        return dateProvider;
    }

    public Collection<Person> getPeople() {
        return persister.findAll();
    }

    public List<Type> getTypes() {
        return asList(Type.values());
    }

    public List<Long> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(List<Long> ids) {
        selectedIds = ids;
    }

    public Collection<Person> getSelectedPeople() {
        List<Person> selected = new ArrayList<Person>();
        for (long id : selectedIds) {
            selected.add(persister.findById(id));
        }
        return selected;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void remove(Long personId) {
        persister.delete(personId);
    }

    @ActionMethod(parameters = { "id" })
    public void select(Long id) {
        this.person = persister.findById(id);
    }

    public void select() {
        this.person = persister.findById(id);
    }

    public void show() {
        // do nothing: the selected Ids and people are automatically populated
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

    public View export() {
        StringBuffer sb = new StringBuffer();
        sb.append("Id,First Name,Last Name,Date of Birth\n");
        for (Person person : getSelectedPeople()) {
            sb.append(person.getId());
            sb.append(",");
            sb.append(person.getFirstName());
            sb.append(",");
            sb.append(person.getLastName());
            sb.append(",");
            sb.append(person.getDateOfBirth());
            sb.append("\n");
        }
        return new ExportView(this, "text/csv", sb.toString().getBytes(), "export.csv");
    }

}
