package org.codehaus.waffle.registrar;

import org.junit.Test;
import org.junit.Assert;
import static org.codehaus.waffle.registrar.SessionAttributeReference.sessionAttribute;

public class SessionAttributeReferenceTest {

    @Test
    public void canConstructInstanceFromSessionMethod() {
        SessionAttributeReference reference = sessionAttribute("foo");
        Assert.assertEquals("foo", reference.getKey());
    }
}
