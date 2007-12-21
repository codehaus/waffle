package org.codehaus.waffle.registrar;

import org.junit.Assert;
import org.junit.Test;
import static org.codehaus.waffle.registrar.ComponentReference.component;

public class ComponentReferenceTest {

    @Test
    public void canConstructInstanceFromComponentMethod() {
        Reference reference = component("foo");
        Assert.assertEquals("foo", reference.getKey());
    }
}
