package org.codehaus.waffle.registrar;

import org.junit.Test;
import org.junit.Assert;

public class RequestParameterReferenceTest {

    @Test
    public void canConstructInstanceFromParameterMethod() {
        Reference reference = RequestParameterReference.requestParameter("foo");
        Assert.assertEquals("foo", reference.getKey());
    }
}
