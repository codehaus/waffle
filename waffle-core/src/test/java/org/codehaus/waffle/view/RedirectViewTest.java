package org.codehaus.waffle.view;

import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;

public class RedirectViewTest {

    @Test
    public void defaultStatusCodeShouldBe303() {
        RedirectView redirectView = new RedirectView("foo", null);
        Assert.assertEquals(HttpServletResponse.SC_SEE_OTHER, redirectView.getStatusCode());
    }
    
    @Test
    public void shouldBeAbleToSetStatusCode() {
        RedirectView redirectView = new RedirectView("foo", null, 1985);
        Assert.assertEquals(1985, redirectView.getStatusCode());
    }
}
