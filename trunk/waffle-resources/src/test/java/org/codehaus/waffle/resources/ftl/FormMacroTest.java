package org.codehaus.waffle.resources.ftl;

import org.apache.commons.io.IOUtils;
import org.codehaus.waffle.resources.ftl.FormController.Displayable;
import static org.codehaus.waffle.testing.view.ViewHarness.processView;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.io.IOException;
import static java.util.Arrays.asList;

/**
 * @author Mauro Talevi
 */
public class FormMacroTest {

    private static final boolean SHOW_VIEW = false;
    
    protected String processTemplateView(Object controller, String templatePath) {
        return processView(templatePath, controller, SHOW_VIEW);
    }

    private String readResource(String path) throws IOException {
        return IOUtils.toString(ClassLoader.getSystemResourceAsStream(path));
    }

    @Test
    public void canProcessSelectMacros() throws IOException{
        FormController controller = new FormController();
        controller.setSelectedValues(asList("one"));
        controller.setSelectedType(FormController.Type.ONE);
        controller.setSelectedDisplayables(asList(new Displayable("two","Two")));
        String expected = readResource("org/codehaus/waffle/resources/ftl/select.txt");
        assertEquals(expected, processTemplateView(controller, "org/codehaus/waffle/resources/ftl/select.ftl"));
    }
    
    @Test
    public void canDecorateOptionsAsNameables() throws IOException{
        FormController controller = new FormController();
        String expected = readResource("org/codehaus/waffle/resources/ftl/nameables.txt");
        assertEquals(expected, processTemplateView(controller, "org/codehaus/waffle/resources/ftl/nameables.ftl"));
    }

}