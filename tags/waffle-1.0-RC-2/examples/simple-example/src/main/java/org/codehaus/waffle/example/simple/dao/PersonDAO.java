package org.codehaus.waffle.example.simple.dao;

import org.codehaus.waffle.example.simple.model.Person;

import java.util.Collection;

public interface PersonDAO {

    Person findById(Long personId);

    Collection<Person> findAll();

    void save(Person person);

    void delete(Long personId);

}
