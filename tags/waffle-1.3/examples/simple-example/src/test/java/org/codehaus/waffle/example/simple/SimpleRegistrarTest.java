package org.codehaus.waffle.example.simple;

import static org.codehaus.waffle.context.ContextLevel.APPLICATION;
import static org.codehaus.waffle.context.ContextLevel.REQUEST;
import static org.codehaus.waffle.context.ContextLevel.SESSION;
import static org.junit.Assert.assertNotNull;

import org.codehaus.waffle.testing.registrar.RegistrarHelper;
import org.junit.Test;

public class SimpleRegistrarTest {

    private static final Class<SimpleRegistrar> CLASS = SimpleRegistrar.class;
    RegistrarHelper helper = new RegistrarHelper();

    @Test
    public void canRegisterComponentsAtDifferentLevels() {
        helper.componentsFor(CLASS, APPLICATION);
        helper.componentsFor(CLASS, SESSION);
        helper.componentsFor(CLASS, REQUEST);
    }

    @Test
    public void canRetrieveControllers() {
        assertNotNull(helper.controllerFor(CLASS, APPLICATION, "helloworld"));
        assertNotNull(helper.controllerFor(CLASS, APPLICATION, "ajaxexample"));
        assertNotNull(helper.controllerFor(CLASS, SESSION, "person"));
        assertNotNull(helper.controllerFor(CLASS, SESSION, "calculator"));
        assertNotNull(helper.controllerFor(CLASS, SESSION, "automobile"));
        assertNotNull(helper.controllerFor(CLASS, REQUEST, "upload"));
    }

}