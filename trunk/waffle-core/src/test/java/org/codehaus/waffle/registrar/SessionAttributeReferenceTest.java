package org.codehaus.waffle.registrar;

import org.junit.Test;
import org.junit.Assert;

public class SessionAttributeReferenceTest {

    @Test
    public void canConstructInstanceFromSessionMethod() {
        Reference reference = SessionAttributeReference.sessionAttribute("foo");
        Assert.assertEquals("foo", reference.getKey());
    }
}
