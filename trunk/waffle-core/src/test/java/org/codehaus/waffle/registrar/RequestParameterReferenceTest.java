package org.codehaus.waffle.registrar;

import org.junit.Test;
import org.junit.Assert;
import static org.codehaus.waffle.registrar.RequestParameterReference.requestParameter;

public class RequestParameterReferenceTest {

    @Test
    public void canConstructInstanceFromParameterMethod() {
        Reference reference = requestParameter("foo");
        Assert.assertEquals("foo", reference.getKey());
    }
}
