package org.codehaus.waffle.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.waffle.bind.StringTransmuter;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.testmodel.SampleForMethodFinder;
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
                stringTransmuter, new SilentMonitor());
    }

    @Test
    public void canReturnDefaultMethod() throws NoSuchMethodException {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue(null));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);

        ControllerWithDefaultActionMethodNoValue controller = new ControllerWithDefaultActionMethodNoValue();
        ServletContext servletContext = null;
        ArgumentResolver argumentResolver = null;
        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(servletContext, argumentResolver,
                methodNameResolver, stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(controller, request, response);

        Method expectedMethod = ControllerWithDefaultActionMethodNoValue.class.getMethod("foobar");
        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

    @Test
    public void canReturnDefaultActionMethodWithArgument() throws NoSuchMethodException {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue(null));
            }
        });

        // Mock ArgumentResolver
        final ArgumentResolver argumentResolver = mockery.mock(ArgumentResolver.class);
        mockery.checking(new Expectations() {
            {
                one(argumentResolver).resolve(request, "{helloworld}");
                will(returnValue("helloworld"));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);

        ControllerWithDefaultActionMethod controller = new ControllerWithDefaultActionMethod();
        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, argumentResolver,
                methodNameResolver, stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(controller, request, response);

        Method expectedMethod = ControllerWithDefaultActionMethod.class.getMethod("foobar", String.class);
        assertEquals(expectedMethod, methodDefinition.getMethod());
        assertEquals("helloworld", methodDefinition.getMethodArguments().get(0));
    }

    @Test
    public void canFindForDefaultActionMethodWillNotReturnSameInstanceOfMethodDefinition() throws Exception {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                exactly(2).of(methodNameResolver).resolve(with(same(request)));
                will(returnValue(null));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);

        ControllerWithDefaultActionMethodNoValue controller = new ControllerWithDefaultActionMethodNoValue();
        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, null, methodNameResolver,
                stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(controller, request, response);

        assertNotSame(methodDefinition, methodDefinitionFinder.find(controller, request, response));
    }

    @Test
    public void canFindActionMethodWithNoArguments() throws NoSuchMethodException {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue("noArgumentMethod"));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, null, methodNameResolver,
                stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class.getMethod("noArgumentMethod");
        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

    @Test
    public void canSupportPragmaticMethod() throws Exception {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue("methodTwo|foobar"));
            }
        });

        // Mock ArgumentResolver
        final ArgumentResolver argumentResolver = mockery.mock(ArgumentResolver.class);
        mockery.checking(new Expectations() {
            {
                one(argumentResolver).resolve(request, "foobar");
                will(returnValue(new ArrayList<Object>()));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, argumentResolver,
                methodNameResolver, stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class.getMethod("methodTwo", List.class);
        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

    @Test
    public void canIgnoreNonPublicMethods() throws Exception {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue("noAmbiguityWhenMethodNotPublic"));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);

        // Mock ArgumentResolver
        final ArgumentResolver argumentResolver = mockery.mock(ArgumentResolver.class);

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, argumentResolver,
                methodNameResolver, stringTransmuter);

        MethodDefinition definition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method methodExpected = SampleForMethodFinder.class.getMethod("noAmbiguityWhenMethodNotPublic",
                HttpServletRequest.class);
        assertEquals(methodExpected, definition.getMethod());
    }

    // FIXME@Test
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

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, argumentResolver,
                methodNameResolver, stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class.getMethod("methodTwo", List.class);
        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

    // FIXME@Test(expected = AmbiguousActionSignatureMethodException.class)
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
                one(argumentResolver).resolve(request, "{list}");
                will(returnValue(new ArrayList<Object>()));
                one(argumentResolver).resolve(request, "{object}");
                will(returnValue(new ArrayList<Object>()));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, argumentResolver,
                methodNameResolver, stringTransmuter);

        methodDefinitionFinder.find(sampleForMethodFinder, request, response);
    }

    @Test(expected = NoMatchingActionMethodException.class)
    public void cannotFindMethodsWithNoName() throws Exception {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue("noSuchMethod"));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, null, methodNameResolver,
                stringTransmuter);

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
                one(argumentResolver).resolve(request, "{integer}");
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

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();

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

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();

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
                one(argumentResolver).resolve(request, "{decimal}");
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

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();

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
                one(argumentResolver).resolve(request, "{bool}");
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

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();

        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, argumentResolver,
                methodNameResolver, stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        assertTrue((Boolean) methodDefinition.getMethodArguments().get(0));
    }

    @Test
    public void canDependOnRequest() throws Exception {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue("methodDependsOnRequest"));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, null, methodNameResolver,
                stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class.getMethod("methodDependsOnRequest",
                HttpServletRequest.class);

        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

    @Test
    public void canDependOnResponse() throws Exception {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue("methodDependsOnResponse"));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, null, methodNameResolver,
                stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class.getMethod("methodDependsOnResponse",
                HttpServletResponse.class);

        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

    @Test
    public void canDependOnRequestAndOtherArgument() throws Exception {
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue("methodDependsOnRequestAndInteger"));
            }
        });

        // Mock ArgumentResolver
        final ArgumentResolver argumentResolver = mockery.mock(ArgumentResolver.class);
        mockery.checking(new Expectations() {
            {
                one(argumentResolver).resolve(request, "{integer}");
                will(returnValue("99"));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);
        mockery.checking(new Expectations() {
            {
                one(stringTransmuter).transmute("99", int.class);
                will(returnValue(99));
            }
        });

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();

        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, argumentResolver,
                methodNameResolver, stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class.getMethod("methodDependsOnRequestAndInteger",
                HttpServletRequest.class, int.class);

        assertEquals(expectedMethod, methodDefinition.getMethod());
        assertEquals(99, methodDefinition.getMethodArguments().get(1));
    }

    @Test
    public void canDependOnSession() throws Exception {
        // Mock HttpSession
        final HttpSession session = mockery.mock(HttpSession.class);

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getSession();
                will(returnValue(session));
            }
        });
        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue("methodDependsOnSession"));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, null, methodNameResolver,
                stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class.getMethod("methodDependsOnSession", HttpSession.class);

        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

    @Test
    public void canDependOnServletContext() throws Exception {
        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodNameResolver
        final MethodNameResolver methodNameResolver = mockery.mock(MethodNameResolver.class);
        mockery.checking(new Expectations() {
            {
                one(methodNameResolver).resolve(with(same(request)));
                will(returnValue("methodDependsOnServletContext"));
            }
        });

        // Mock StringTransmuter
        final StringTransmuter stringTransmuter = mockery.mock(StringTransmuter.class);

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(servletContext, null,
                methodNameResolver, stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class.getMethod("methodDependsOnServletContext",
                ServletContext.class);

        assertEquals(expectedMethod, methodDefinition.getMethod());
        assertSame(servletContext, methodDefinition.getMethodArguments().get(0));
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
                one(stringTransmuter).transmute("blah", parameterTypeForMethod("listOfStrings"));
                will(returnValue(Collections.EMPTY_LIST));
            }
        });

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = newMethodDefinitionFinder(null, argumentResolver,
                methodNameResolver, stringTransmuter);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class.getMethod("methodListOfStrings", List.class);
        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

}
