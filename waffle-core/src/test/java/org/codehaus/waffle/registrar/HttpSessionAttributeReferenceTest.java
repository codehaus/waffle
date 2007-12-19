package org.codehaus.waffle.registrar;

import org.junit.Test;
import org.junit.Assert;

public class HttpSessionAttributeReferenceTest {

    @Test
    public void canConstructInstanceFromSessionMethod() {
        Reference reference = HttpSessionAttributeReference.session("foo");
        Assert.assertEquals("foo", reference.getKey());
    }
}
