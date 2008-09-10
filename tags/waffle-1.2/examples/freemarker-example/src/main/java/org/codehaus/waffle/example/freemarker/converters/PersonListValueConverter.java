package org.codehaus.waffle.example.freemarker.converters;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.example.freemarker.model.Person;
import org.codehaus.waffle.example.freemarker.persister.PersonPersister;

/**
 * ValueConverter for person lists as retrieved from persistable id
 * 
 * @author Mauro Talevi
 */
public class PersonListValueConverter implements ValueConverter {

    private final PersonPersister persister;

    public PersonListValueConverter(PersonPersister persister){
        this.persister = persister;        
    }
    
    public boolean accept(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            Type argumentType = parameterizedType.getActualTypeArguments()[0];
            return List.class.isAssignableFrom((Class<?>) rawType)
                    && Person.class.isAssignableFrom((Class<?>) argumentType);
        }
        return false;
    }

    public Object convertValue(String propertyName, String value, Type toType) {
        String[] values = value.split(",");
        List<Person> list = new ArrayList<Person>();
        for (String current : values) {
            if (current.trim().length() > 0) {
                list.add(persister.findById(Long.valueOf(current)));
            }
        }
        return list;
    }

}
