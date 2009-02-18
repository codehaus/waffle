package org.codehaus.waffle.context;

import org.junit.Test;
import org.junit.Assert;
import static org.codehaus.waffle.context.ServletContextAttributeReference.servletContextAttribute;
import org.codehaus.waffle.context.ServletContextAttributeReference;

public class ServletContextAttributeReferenceTest {

    @Test
    public void canConstructInstanceFromParameterMethod() {
        ServletContextAttributeReference reference = servletContextAttribute("foo");
        Assert.assertEquals("foo", reference.getKey());
    }
}
