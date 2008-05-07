package org.codehaus.waffle.testing.view;

import static org.codehaus.waffle.testing.view.ViewHarness.processView;

import org.codehaus.waffle.testing.ListController;
import org.junit.Test;

/**
 * @author Mauro Talevi
 */
public class ViewHarnessTest {

    @Test
    public void canProcessFreemarkerTemplate() {
        ListController controller = new ListController();
        controller.list();
        processView("freemarker/list.ftl", controller, false);
    }


}
