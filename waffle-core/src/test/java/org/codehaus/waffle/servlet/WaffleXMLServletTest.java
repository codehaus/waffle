package org.codehaus.waffle.servlet;

import static org.junit.Assert.assertTrue;

import org.codehaus.waffle.action.ActionMethodResponse;
import org.codehaus.waffle.view.View;
import org.codehaus.waffle.view.XMLView;
import org.junit.Test;

public class WaffleXMLServletTest {

    @Test
    public void canOverridActionMethodResponse() {
        ActionMethodResponse response = new ActionMethodResponse();
        response.setReturnValue("a value that will be overriden....");

        WaffleXMLServlet servlet = new WaffleXMLServlet();
        View view = servlet.buildViewToReferrer();
        assertTrue(view instanceof XMLView);
    }

}
