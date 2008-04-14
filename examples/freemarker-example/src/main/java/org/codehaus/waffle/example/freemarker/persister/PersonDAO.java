package org.codehaus.waffle.example.freemarker.persister;

import org.codehaus.waffle.example.freemarker.model.Person;

import java.util.Collection;

public interface PersonDAO {

    Person findById(Long personId);

    Collection<Person> findAll();

    void save(Person person);

    void delete(Long personId);

}
