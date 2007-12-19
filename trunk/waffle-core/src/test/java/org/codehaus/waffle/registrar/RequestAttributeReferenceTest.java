package org.codehaus.waffle.registrar;

import org.junit.Test;
import org.junit.Assert;

public class RequestAttributeReferenceTest {

    @Test
    public void canConstructInstanceFromRequestMethod() {
        Reference reference = RequestAttributeReference.requestAttribute("foo");
        Assert.assertEquals("foo", reference.getKey());
    }
}
