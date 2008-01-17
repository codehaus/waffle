package org.codehaus.waffle.registrar;

import static org.codehaus.waffle.registrar.RequestParameterReference.requestParameter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class RequestParameterReferenceTest {

    @Test
    public void canConstructInstanceFromParameterMethod() {
        RequestParameterReference reference = requestParameter("foo");
        assertEquals("foo", reference.getKey());
    }
}
