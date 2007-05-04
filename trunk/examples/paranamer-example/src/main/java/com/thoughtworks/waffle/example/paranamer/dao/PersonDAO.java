package com.thoughtworks.waffle.example.paranamer.dao;

import com.thoughtworks.waffle.example.paranamer.model.Person;

import java.util.Collection;

public interface PersonDAO {

    Person findById(Long personId);

    Collection<Person> findAll();

    void save(Person person);

    void delete(Long personId);

}
