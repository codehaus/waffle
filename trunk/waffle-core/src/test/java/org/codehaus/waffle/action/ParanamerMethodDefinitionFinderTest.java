package org.codehaus.waffle.action;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.bind.StringTransmuter;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.testmodel.FakeControllerWithMethodDefinitions;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Paul Hammant
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class ParanamerMethodDefinitionFinderTest extends AbstractMethodDefinitionFinderTest {

    private Mockery mockery = new Mockery();

    protected MethodDefinitionFinder newMethodDefinitionFinder(ServletContext servletContext,
            ArgumentResolver argumentResolver, MethodNameResolver methodNameResolver, StringTransmuter stringTransmuter) {
        return new ParanamerMethodDefinitionFinder(servletContext, argumentResolver, methodNameResolver,
                stringTransmuter, new SilentMonitor(), new DefaultMessageResources());
    }

    @Test
    public void canUseCustomStringTransmuter() throws Exception {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue("methodListOfStrings|blah"));
            }
        });

        // Mock ArgumentResolver
        final ArgumentResolver argumentResolver = mockery.mock(ArgumentResolver.class);
        mockery.checking(new Expectations() {
            {
                one(argumentResolver).resolve(request, "blah");
                will(returnValue("blah"));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);
        mockery.checking(new Expectations() {
            {
                one(stringTransmuter).transmute("blah", List.class);
                will(returnValue(Collections.EMPTY_LIST));
            }
        });

        FakeControllerWithMethodDefinitions controller = new FakeControllerWithMethodDefinitions();
        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, argumentResolver,
                methodNameResolver, stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(controller, request, response);

        Method expectedMethod = FakeControllerWithMethodDefinitions.class.getMethod("methodListOfStrings", List.class);
        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

}
