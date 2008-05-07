package org.codehaus.waffle.example.freemarker;

import static java.util.Arrays.asList;

import java.util.Collections;

import javax.servlet.ServletContext;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.bind.converters.DateValueConverter;
import org.codehaus.waffle.context.pico.PicoComponentRegistry;
import org.codehaus.waffle.mock.PicoRegistrarMockery;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.jmock.Expectations;
import org.junit.Test;

public class FreemarkerRegistrarTest {

    @SuppressWarnings("unchecked")
    @Test
    public void canAssertConfiguration() {
        PicoRegistrarMockery mockery = new PicoRegistrarMockery();
        final ServletContext context = mockery.mockServletContext();
        mockery.checking(new Expectations(){{
            atLeast(1).of(context).getInitParameterNames();
            will(returnValue(Collections.enumeration(asList("register:DateValueConverter"))));
            atLeast(1).of(context).getInitParameter(with(notEqual("register:DateValueConverter")));
            will(returnValue(null));
            atLeast(1).of(context).getInitParameter(with(equal("register:DateValueConverter")));
            will(returnValue(DateValueConverter.class.getName()));
        }});
        mockery.checking(new Expectations(){{
            atLeast(1).of(context).getAttribute(ComponentRegistry.class.getName());
            will(returnValue(new PicoComponentRegistry(context)));
        }});
        mockery.assertConfiguration(FreemarkerRegistrar.class);
    }

    public static <T> Matcher<T> notEqual(T value) {
        return new IsNot<T>(new IsEqual<T>(value));
    }
}
