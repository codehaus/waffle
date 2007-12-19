package org.codehaus.waffle.registrar;

import org.junit.Assert;
import org.junit.Test;

public class ComponentReferenceTest {

    @Test
    public void canConstructInstanceFromComponentMethod() {
        Reference reference = ComponentReference.component("foo");
        Assert.assertEquals("foo", reference.getKey());
    }
}
