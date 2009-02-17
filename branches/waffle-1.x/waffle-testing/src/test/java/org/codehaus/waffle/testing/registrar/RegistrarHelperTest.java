package org.codehaus.waffle.testing.registrar;

import static org.junit.Assert.assertNotNull;

import org.codehaus.waffle.context.ContextLevel;
import org.junit.Test;

/**
 * @author Mauro Talevi
 */
public class RegistrarHelperTest {

    @Test
    public void canRegisterComponentsAtDifferentLevels() {
        RegistrarHelper helper = new RegistrarHelper();
        helper.componentsFor(MyRegistrar.class, ContextLevel.APPLICATION);
    }

    @Test
    public void canRetrieveControllers() {
        RegistrarHelper helper = new RegistrarHelper();
        assertNotNull(helper.controllerFor(MyRegistrar.class, ContextLevel.APPLICATION, "list"));
    }

}
