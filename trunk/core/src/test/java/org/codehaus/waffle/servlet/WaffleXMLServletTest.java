package org.codehaus.waffle.servlet;

import org.codehaus.waffle.action.ActionMethodResponse;
import org.codehaus.waffle.view.XMLView;
import org.codehaus.waffle.view.View;
import org.junit.Assert;
import org.junit.Test;

public class WaffleXMLServletTest {

    @Test
    public void testOverridenActionMethodResponse() {
        ActionMethodResponse response = new ActionMethodResponse();
        response.setReturnValue("a value that will be overriden....");

        WaffleXMLServlet servlet = new WaffleXMLServlet();
        View view = servlet.buildViewToReferrer(null);
        Assert.assertTrue(view instanceof XMLView);
    }

}
