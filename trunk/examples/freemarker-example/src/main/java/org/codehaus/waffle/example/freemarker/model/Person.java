package org.codehaus.waffle.example.freemarker.model;

import java.util.Date;

public interface Person {

    public Long getId();

    public String getFirstName();

    public String getLastName();

    public String getEmail();

    public Date getDateOfBirth();

}
