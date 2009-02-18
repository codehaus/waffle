package org.codehaus.waffle.context;

import org.junit.Assert;
import org.junit.Test;
import static org.codehaus.waffle.context.ComponentReference.component;
import org.codehaus.waffle.context.Reference;

public class ComponentReferenceTest {

    @Test
    public void canConstructInstanceFromComponentMethod() {
        Reference reference = component("foo");
        Assert.assertEquals("foo", reference.getKey());
    }
}
