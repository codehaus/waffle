package org.codehaus.waffle.testing.view;

import static org.codehaus.waffle.testing.view.ViewHarness.exportView;
import static org.codehaus.waffle.testing.view.ViewHarness.processView;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.codehaus.waffle.testing.ListController;
import org.junit.Test;

/**
 * @author Mauro Talevi
 */
public class ViewHarnessTest {

    @Test
    public void canProcessFreemarkerView() {
        ListController controller = new ListController();
        controller.list();
        processView("freemarker/list.ftl", controller, false);
    }

    @Test
    public void canExportView() throws IOException {
        ListController controller = new ListController();
        controller.list();
        File output = new File("target/export/list.html");
        exportView("freemarker/list.ftl", controller, output);
        assertTrue(output.exists());
    }

    
}
