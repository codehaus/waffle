package org.codehaus.waffle.example.simple;

import org.codehaus.waffle.mock.PicoRegistrarMockery;
import org.jmock.Expectations;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

public class SimpleRegistrarTest {

    @Test
    public void canAssertConfiguration() {
        PicoRegistrarMockery picoRegistrarMockery = new PicoRegistrarMockery();
        final HttpServletRequest request = picoRegistrarMockery.mockHttpServletRequest();

        picoRegistrarMockery.checking(new Expectations() {
            {
                exactly(2).of(request).getParameter("age");
                will(returnValue("99"));
            }
        });

        picoRegistrarMockery.assertConfiguration(SimpleRegistrar.class);
    }

}
