package org.codehaus.waffle.registrar;

import static org.codehaus.waffle.registrar.RequestParameterReference.requestParameter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class RequestParameterReferenceTest {

    @Test
    public void canConstructInstanceFromParameterMethod() {
        RequestParameterReference reference = requestParameter("foo");
        assertEquals("foo", reference.getKey());
        assertEquals(null, reference.getDefaultValue()); // default is null
    }

    @Test
    public void canAllowForDefaultValueWhenParameterValueIsNull() {
        RequestParameterReference reference = requestParameter("foo", "bar");
        assertEquals("foo", reference.getKey());
        assertEquals("bar", reference.getDefaultValue());
    }

}