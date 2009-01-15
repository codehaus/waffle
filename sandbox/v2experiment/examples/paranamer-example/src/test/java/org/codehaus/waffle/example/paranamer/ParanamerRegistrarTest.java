package org.codehaus.waffle.example.paranamer;

import static org.codehaus.waffle.context.ContextLevel.APPLICATION;
import static org.codehaus.waffle.context.ContextLevel.REQUEST;
import static org.codehaus.waffle.context.ContextLevel.SESSION;
import static org.junit.Assert.assertNotNull;

import org.codehaus.waffle.testing.registrar.RegistrarHelper;
import org.junit.Test;

public class ParanamerRegistrarTest {

    private static final Class<ParanamerRegistrar> CLASS = ParanamerRegistrar.class;

    @Test
    public void canRegisterComponentsAtDifferentLevels() {
        RegistrarHelper helper = new RegistrarHelper();
        helper.componentsFor(CLASS, APPLICATION);
        helper.componentsFor(CLASS, SESSION);
        helper.componentsFor(CLASS, REQUEST);
    }

    @Test
    public void canRetrieveControllers() {
        RegistrarHelper helper = new RegistrarHelper();
        assertNotNull(helper.controllerFor(CLASS, APPLICATION, "helloworld"));
        assertNotNull(helper.controllerFor(CLASS, APPLICATION, "ajaxexample"));
        assertNotNull(helper.controllerFor(CLASS, APPLICATION, "people/person"));
        assertNotNull(helper.controllerFor(CLASS, SESSION, "calculator"));
    }
}
