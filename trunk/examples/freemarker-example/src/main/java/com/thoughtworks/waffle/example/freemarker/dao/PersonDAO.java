package com.thoughtworks.waffle.example.freemarker.dao;

import com.thoughtworks.waffle.example.freemarker.model.Person;

import java.util.Collection;

public interface PersonDAO {

    Person findById(Long personId);

    Collection<Person> findAll();

    void save(Person person);

    void delete(Long personId);

}
