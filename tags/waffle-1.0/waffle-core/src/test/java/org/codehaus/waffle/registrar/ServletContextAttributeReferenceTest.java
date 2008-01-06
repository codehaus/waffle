package org.codehaus.waffle.registrar;

import org.junit.Test;
import org.junit.Assert;
import static org.codehaus.waffle.registrar.ServletContextAttributeReference.servletContextAttribute;

public class ServletContextAttributeReferenceTest {

    @Test
    public void canConstructInstanceFromParameterMethod() {
        Reference reference = servletContextAttribute("foo");
        Assert.assertEquals("foo", reference.getKey());
    }
}
