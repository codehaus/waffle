package org.codehaus.waffle.example.freemarker.persister;

import org.codehaus.waffle.example.freemarker.model.Person;
import org.codehaus.waffle.example.freemarker.persister.PersonPersister;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SimplePersonPersister implements PersonPersister {
    private static long COUNT = 3;
    private final Map<Long, Person> map = new HashMap<Long, Person>();

    public SimplePersonPersister() {
        loadPeople();
    }

    private void loadPeople() {
        addPerson(1L, "Harry", "Potter", "hp@gryffindor.com");
        addPerson(2L, "Ronald", "Weasly", "rw@gryffindor.com");
        addPerson(3L, "Hermione", "Granger", "hg@gryffindor.com");
    }

    private void addPerson(Long id, String firstName, String lastName, String email) {
        PersistablePerson person = new PersistablePerson();
        person.setId(id);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setEmail(email);
        map.put(id, person);
    }

    public Person findById(Long personId) {
        // create a copy so that edits don't propogate to the map
        return new PersistablePerson(map.get(personId));
    }

    public Collection<Person> findAll() {
        return map.values();
    }

    public void save(Person person) {
        if ( person instanceof PersistablePerson ){            
            if(person.getId().equals(new Long(0))) {
                ((PersistablePerson)person).setId(++COUNT);
            }
            map.put(person.getId(), person);
        }
    }

    public void delete(Long personId) {

    }
}
