package org.codehaus.waffle.registrar;

import org.junit.Test;
import org.junit.Assert;
import static org.codehaus.waffle.registrar.RequestAttributeReference.requestAttribute;

public class RequestAttributeReferenceTest {

    @Test
    public void canConstructInstanceFromRequestMethod() {
        RequestAttributeReference reference = requestAttribute("foo");
        Assert.assertEquals("foo", reference.getKey());
    }
}
