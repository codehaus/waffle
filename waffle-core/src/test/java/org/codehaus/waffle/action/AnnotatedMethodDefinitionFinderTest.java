package org.codehaus.waffle.action;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
                one(argumentResolver).resolve(request, "{foobaz}");
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
                exactly(2).of(argumentResolver).resolve(request, "{foobaz}");
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
    
    @Test
    public void canConvertStringToInteger() throws Exception {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue("methodInteger"));
            }
        });

        // Mock ArgumentResolver
        final ArgumentResolver argumentResolver = mockery.mock(ArgumentResolver.class);
        mockery.checking(new Expectations() {
            {
                one(argumentResolver).resolve(request, "{foobaz}");
                will(returnValue("45"));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);
        mockery.checking(new Expectations() {
            {
                one(stringTransmuter).transmute("45", int.class);
                will(returnValue(45));
            }
        });

        FakeControllerWithMethodDefinitions sampleForMethodFinder = new FakeControllerWithMethodDefinitions();

        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, argumentResolver,
                methodNameResolver, stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);
        assertEquals(45, methodDefinition.getMethodArguments().get(0));
    }

    @Test
    public void canConvertStringToIntegerPragmatic() throws Exception {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue("methodInteger|45"));
            }
        });

        // Mock ArgumentResolver
        final ArgumentResolver argumentResolver = mockery.mock(ArgumentResolver.class);
        mockery.checking(new Expectations() {
            {
                one(argumentResolver).resolve(request, "45");
                will(returnValue("45"));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);
        mockery.checking(new Expectations() {
            {
                one(stringTransmuter).transmute("45", int.class);
                will(returnValue(45));
            }
        });

        FakeControllerWithMethodDefinitions sampleForMethodFinder = new FakeControllerWithMethodDefinitions();

        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, argumentResolver,
                methodNameResolver, stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);
        assertEquals(45, methodDefinition.getMethodArguments().get(0));
    }

    @Test
    public void canConvertStringToFloat() throws Exception {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue("methodFloat"));
            }
        });

        // Mock ArgumentResolver
        final ArgumentResolver argumentResolver = mockery.mock(ArgumentResolver.class);
        mockery.checking(new Expectations() {
            {
                one(argumentResolver).resolve(request, "{foobaz}");
                will(returnValue("99.99"));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);
        mockery.checking(new Expectations() {
            {
                one(stringTransmuter).transmute("99.99", Float.class);
                will(returnValue(99.99f));
            }
        });

        FakeControllerWithMethodDefinitions sampleForMethodFinder = new FakeControllerWithMethodDefinitions();

        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, argumentResolver,
                methodNameResolver, stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        assertEquals(99.99f, methodDefinition.getMethodArguments().get(0));
    }

    @Test
    public void canConvertStringToBoolean() throws Exception {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue("methodBoolean"));
            }
        });

        // Mock ArgumentResolver
        final ArgumentResolver argumentResolver = mockery.mock(ArgumentResolver.class);
        mockery.checking(new Expectations() {
            {
                one(argumentResolver).resolve(request, "{foobaz}");
                will(returnValue("true"));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);
        mockery.checking(new Expectations() {
            {
                one(stringTransmuter).transmute("true", boolean.class);
                will(returnValue(true));
            }
        });

        FakeControllerWithMethodDefinitions sampleForMethodFinder = new FakeControllerWithMethodDefinitions();

        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, argumentResolver,
                methodNameResolver, stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        assertTrue((Boolean) methodDefinition.getMethodArguments().get(0));
    }

    @Test
    public void canConvertStringToListOfStrings() throws Exception {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue("methodListOfStrings"));
            }
        });

        final List<String> list = asList("one", "two");
        // Mock ArgumentResolver
        final ArgumentResolver argumentResolver = mockery.mock(ArgumentResolver.class);
        mockery.checking(new Expectations() {
            {
                one(argumentResolver).resolve(request, "{foobaz}");
                will(returnValue("one,two"));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);
        mockery.checking(new Expectations() {
            {
                one(stringTransmuter).transmute("one,two", parameterTypeForMethod(ControllerWithListMethods.class, "listOfStrings"));
                will(returnValue(list));
            }
        });

        FakeControllerWithMethodDefinitions sampleForMethodFinder = new FakeControllerWithMethodDefinitions();

        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, argumentResolver,
                methodNameResolver, stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        assertEquals(list, methodDefinition.getMethodArguments().get(0));
    }

    @Test
    public void canConvertStringToListOfIntegers() throws Exception {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue("methodListOfIntegers"));
            }
        });

        final List<Integer> list = asList(1, 2);
        // Mock ArgumentResolver
        final ArgumentResolver argumentResolver = mockery.mock(ArgumentResolver.class);
        mockery.checking(new Expectations() {
            {
                one(argumentResolver).resolve(request, "{foobaz}");
                will(returnValue("1,2"));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);
        mockery.checking(new Expectations() {
            {
                one(stringTransmuter).transmute("1,2", parameterTypeForMethod(ControllerWithListMethods.class, "listOfIntegers"));
                will(returnValue(list));
            }
        });

        FakeControllerWithMethodDefinitions sampleForMethodFinder = new FakeControllerWithMethodDefinitions();

        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, argumentResolver,
                methodNameResolver, stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        assertEquals(list, methodDefinition.getMethodArguments().get(0));
    }

}
