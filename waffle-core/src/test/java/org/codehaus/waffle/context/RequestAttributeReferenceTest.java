package org.codehaus.waffle.context;

import org.junit.Test;
import org.junit.Assert;
import static org.codehaus.waffle.context.RequestAttributeReference.requestAttribute;
import org.codehaus.waffle.context.RequestAttributeReference;

public class RequestAttributeReferenceTest {

    @Test
    public void canConstructInstanceFromRequestMethod() {
        RequestAttributeReference reference = requestAttribute("foo");
        Assert.assertEquals("foo", reference.getKey());
    }
}
