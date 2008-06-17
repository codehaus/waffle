package org.codehaus.waffle.action;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.bind.StringTransmuter;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.testmodel.FakeControllerWithMethodDefinitions;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class AnnotatedMethodDefinitionFinderTest extends AbstractMethodDefinitionFinderTest {

    Mockery mockery = new Mockery();

    protected MethodDefinitionFinder newMethodDefinitionFinder(ServletContext servletContext,
            final ArgumentResolver argumentResolver, final MethodNameResolver methodNameResolver,
            final StringTransmuter stringTransmuter) {
        return new AnnotatedMethodDefinitionFinder(servletContext, argumentResolver, methodNameResolver, stringTransmuter,
                new SilentMonitor());
    }

   
    @Test
    public void canFindMethodWhenParameterAssignable() throws Exception {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue("methodTwo"));
            }
        });

        // Mock ArgumentResolver
        final ArgumentResolver argumentResolver = mockery.mock(ArgumentResolver.class);
        mockery.checking(new Expectations() {
            {
                one(argumentResolver).resolve(request, "{list}");
                will(returnValue(new ArrayList<Object>()));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);

        FakeControllerWithMethodDefinitions sampleForMethodFinder = new FakeControllerWithMethodDefinitions();
        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, argumentResolver,
                methodNameResolver, stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = FakeControllerWithMethodDefinitions.class.getMethod("methodTwo", List.class);
        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

    @Test(expected = AmbiguousActionSignatureMethodException.class)
    public void cannotAllowAmbiguity() throws Exception {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue("methodAmbiguous"));
            }
        });

        // Mock ArgumentResolver
        final ArgumentResolver argumentResolver = mockery.mock(ArgumentResolver.class);
        mockery.checking(new Expectations() {
            {
                one(argumentResolver).resolve(request, "{object}");
                will(returnValue(new ArrayList<Object>()));
                one(argumentResolver).resolve(request, "{list}");
                will(returnValue(new ArrayList<Object>()));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);

        FakeControllerWithMethodDefinitions sampleForMethodFinder = new FakeControllerWithMethodDefinitions();
        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, argumentResolver,
                methodNameResolver, stringTransmuter);

        methodDefinitionFinder.find(sampleForMethodFinder, request, response);
    }

}
