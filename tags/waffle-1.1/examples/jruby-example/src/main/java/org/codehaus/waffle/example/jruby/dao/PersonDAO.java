package org.codehaus.waffle.example.jruby.dao;

import org.codehaus.waffle.example.jruby.model.Person;

import java.util.Collection;

public interface PersonDAO {
    
    Person findById(Long personId);

    Collection<Person> findAll();

    void save(Person person);

    void delete(Long personId);

}
