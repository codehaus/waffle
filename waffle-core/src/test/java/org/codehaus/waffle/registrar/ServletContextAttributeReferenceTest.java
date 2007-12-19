package org.codehaus.waffle.registrar;

import org.junit.Test;
import org.junit.Assert;

public class ServletContextAttributeReferenceTest {

    @Test
    public void canConstructInstanceFromParameterMethod() {
        Reference reference = ServletContextAttributeReference.servletContext("foo");
        Assert.assertEquals("foo", reference.getKey());
    }
}
