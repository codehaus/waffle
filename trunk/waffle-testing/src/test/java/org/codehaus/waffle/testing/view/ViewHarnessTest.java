package org.codehaus.waffle.testing.view;

import static org.codehaus.waffle.testing.view.ViewHarness.decorateView;
import static org.codehaus.waffle.testing.view.ViewHarness.exportView;
import static org.codehaus.waffle.testing.view.ViewHarness.processView;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.codehaus.waffle.testing.Controller;
import org.codehaus.waffle.testing.ListController;
import org.codehaus.waffle.testing.MapController;
import org.junit.Test;

/**
 * @author Mauro Talevi
 */
public class ViewHarnessTest {

    @Test
    public void canProcessFreemarkerView() {
        assertViewProcessed("freemarker/list.ftl", new ListController());
        assertViewProcessed("freemarker/map.ftl", new MapController());
    }

    private void assertViewProcessed(String resource, Controller controller) {
        controller.process();
        String processed = processView(resource, controller, false);
        assertTrue(processed.length() > 0);
    }

    @Test
    public void canDecorateWithSitemesh() {
        ListController controller = new ListController();
        controller.process();
        String decorated = decorateView("freemarker/list.ftl", controller, "WEB-INF/testing-decorators.xml", "layout",
                new HashMap<String, Object>());
        assertTrue(decorated.length() > 0);
    }

    @Test
    public void canExportViews() throws IOException {
        ListController controller = new ListController();
        controller.process();
        File list = new File("target/export/list.html");
        exportView(processView("freemarker/list.ftl", controller, false), list);
        assertTrue(list.exists());
        File decorated = new File("target/export/decorated-list.html");
        HashMap<String, Object> decoratorDataModel = new HashMap<String, Object>();
        decoratorDataModel.put("base", "..");
        exportView(decorateView("freemarker/list.ftl", controller, "WEB-INF/testing-decorators.xml", "layout",
                decoratorDataModel), decorated);
        assertTrue(decorated.exists());
    }
}
