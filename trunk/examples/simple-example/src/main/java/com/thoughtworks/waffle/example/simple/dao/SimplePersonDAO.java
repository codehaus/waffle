package com.thoughtworks.waffle.example.simple.dao;

import com.thoughtworks.waffle.example.simple.dao.PersonDAO;
import com.thoughtworks.waffle.example.simple.model.Person;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SimplePersonDAO implements PersonDAO {
    private static long COUNT = 3;
    private final Map<Long, Person> map = new HashMap<Long, Person>();

    public SimplePersonDAO() {
        Person person = new Person();
        person.setId(1L);
        person.setFirstName("Harry");
        person.setLastName("Potter");
        person.setEmail("hp@gryffindor.com");
        map.put(1L, person);

        person = new Person();
        person.setId(2L);
        person.setFirstName("Ronald");
        person.setLastName("Weasly");
        person.setEmail("rw@gryffindor.com");
        map.put(2L, person);

        person = new Person();
        person.setId(3L);
        person.setFirstName("Hermione");
        person.setLastName("Granger");
        person.setEmail("hg@gryffindor.com");
        map.put(3L, person);
    }

    public Person findById(Long personId) {
        // create a copy so that edits don't propogate to the map
        return new Person(map.get(personId));
    }

    public Collection<Person> findAll() {
        return map.values();
    }

    public void save(Person person) {
        if(person.getId() == null) {
            person.setId(++COUNT);
        }
        map.put(person.getId(), person);
    }

    public void delete(Long personId) {

    }
}
