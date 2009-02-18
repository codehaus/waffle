package org.codehaus.waffle.context;

import org.junit.Test;
import org.junit.Assert;
import static org.codehaus.waffle.context.SessionAttributeReference.sessionAttribute;
import org.codehaus.waffle.context.SessionAttributeReference;

public class SessionAttributeReferenceTest {

    @Test
    public void canConstructInstanceFromSessionMethod() {
        SessionAttributeReference reference = sessionAttribute("foo");
        Assert.assertEquals("foo", reference.getKey());
    }
}
