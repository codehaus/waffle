package org.codehaus.waffle.example.freemarker.converters;

import java.lang.reflect.Type;

import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.example.freemarker.model.Person;
import org.codehaus.waffle.example.freemarker.persister.PersonPersister;

/**
 * ValueConverter for person lists as retrieved from persistable id
 * 
 * @author Mauro Talevi
 */
public class PersonValueConverter implements ValueConverter {

    private final PersonPersister persister;

    public PersonValueConverter(PersonPersister persister){
        this.persister = persister;        
    }
    
    public boolean accept(Type type) {
        if (type instanceof Class) {
            return Person.class.isAssignableFrom((Class<?>) type);
        }
        return false;
    }

    public Object convertValue(String propertyName, String value, Type toType) {
        String[] values = value.split(",");
        for (String current : values) {
            if (current.trim().length() > 0) {
                return persister.findById(Long.valueOf(current.trim()));
            }
        }
        return null;
    }

}
