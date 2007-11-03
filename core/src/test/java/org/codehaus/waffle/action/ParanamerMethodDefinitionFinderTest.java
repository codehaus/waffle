package org.codehaus.waffle.action;

import ognl.DefaultTypeConverter;
import ognl.TypeConverter;
import org.codehaus.waffle.action.annotation.DefaultActionMethod;
import org.codehaus.waffle.monitor.ActionMonitor;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.testmodel.SampleForMethodFinder;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Constraint;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//TODO refactor this test to re-use the AnnotatedMethodDefinitionTest
public class ParanamerMethodDefinitionFinderTest extends MockObjectTestCase {

    private ActionMonitor monitor = new SilentMonitor();

    public void testDefaultMethodReturned() throws NoSuchMethodException {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue(null));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        ControllerWithDefaultActionMethodNoValue controller = new ControllerWithDefaultActionMethodNoValue();
        MethodDefinitionFinder methodDefinitionFinder = new ParanamerMethodDefinitionFinder(null, null, null, methodNameResolver, monitor);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(controller, request, response);

        Method expectedMethod = ControllerWithDefaultActionMethodNoValue.class.getMethod("foobar");
        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

    public void testDefaultActionMethodWithArgumentReturned() throws NoSuchMethodException {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue(null));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        // Mock ArgumentResolver
        Mock mockArgumentResolver = mock(ArgumentResolver.class);
        mockArgumentResolver.expects(once())
                .method("resolve")
                .with(same(request), eq("{helloworld}"))
                .will(returnValue("helloworld"));
        ArgumentResolver argumentResolver = (ArgumentResolver) mockArgumentResolver.proxy();

        ControllerWithDefaultActionMethod controller = new ControllerWithDefaultActionMethod();
        MethodDefinitionFinder methodDefinitionFinder = new ParanamerMethodDefinitionFinder(null, argumentResolver, null, methodNameResolver, monitor);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(controller, request, response);

        Method expectedMethod = ControllerWithDefaultActionMethod.class.getMethod("foobar", String.class);
        assertEquals(expectedMethod, methodDefinition.getMethod());
        assertEquals("helloworld", methodDefinition.getMethodArguments().get(0));
    }

    public void testFindActionMethodWithNoArguments() throws NoSuchMethodException {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue("noArgumentMethod"));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = new ParanamerMethodDefinitionFinder(null, null, null, methodNameResolver, monitor);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class.getMethod("noArgumentMethod");
        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

    public void testPragmaticMethodIsSupported() throws Exception {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue("methodTwo|foobar"));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        // Mock ArgumentResolver
        Mock mockArgumentResolver = mock(ArgumentResolver.class);
        mockArgumentResolver.expects(once())
                .method("resolve")
                .with(same(request), eq("foobar"))
                .will(returnValue(new ArrayList()));
        ArgumentResolver argumentResolver = (ArgumentResolver) mockArgumentResolver.proxy();

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = new ParanamerMethodDefinitionFinder(null, argumentResolver, null, methodNameResolver, monitor);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class.getMethod("methodTwo", List.class);
        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

    public void testNonPublicMethodsIgnored() throws Exception {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue("noAmbiguityWhenMethodNotPublic"));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        // Mock ArgumentResolver
        Mock mockArgumentResolver = mock(ArgumentResolver.class);
        ArgumentResolver argumentResolver = (ArgumentResolver) mockArgumentResolver.proxy();

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = new ParanamerMethodDefinitionFinder(null, argumentResolver, null, methodNameResolver, monitor);

        MethodDefinition definition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method methodExpected = SampleForMethodFinder.class.getMethod("noAmbiguityWhenMethodNotPublic", HttpServletRequest.class);
        assertEquals(methodExpected, definition.getMethod());
    }

    public void testFindMethodWhenParameterAssignable() throws Exception {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue("methodTwo"));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        // Mock ArgumentResolver
        Mock mockArgumentResolver = mock(ArgumentResolver.class);
        mockArgumentResolver.expects(once())
                .method("resolve")
                .with(same(request), eq("{list}"))
                .will(returnValue(new ArrayList()));
        ArgumentResolver argumentResolver = (ArgumentResolver) mockArgumentResolver.proxy();

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = new ParanamerMethodDefinitionFinder(null, argumentResolver, null, methodNameResolver, monitor);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class.getMethod("methodTwo", List.class);
        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

    public void testForAmbiguity() throws Exception {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue("methodAmbiguous"));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        // Mock ArgumentResolver
        Mock mockArgumentResolver = mock(ArgumentResolver.class);
        mockArgumentResolver.expects(once())
                .method("resolve")
                .with(same(request), eq("{list}"))
                .will(returnValue(new ArrayList()));
        mockArgumentResolver.expects(once())
                .method("resolve")
                .with(same(request), eq("{object}"))
                .will(returnValue(new ArrayList()));
        ArgumentResolver argumentResolver = (ArgumentResolver) mockArgumentResolver.proxy();

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = new ParanamerMethodDefinitionFinder(null, argumentResolver, null, methodNameResolver, monitor);

        try {
            methodDefinitionFinder.find(sampleForMethodFinder, request, response);
            fail("AmbiguousMethodSignatureException expected");
        } catch (AmbiguousActionSignatureMethodException expected) {

        }
    }

    public void testNoMethodsWithName() throws Exception {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue("noSuchMethod"));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = new ParanamerMethodDefinitionFinder(null, null, null, methodNameResolver, monitor);

        try {
            methodDefinitionFinder.find(sampleForMethodFinder, request, response);
            fail("NoMatchingMethodException expected");
        } catch (NoMatchingActionMethodException expected) {
            // expected
        }
    }

    public void testMethodConvertsStringToInteger() throws Exception {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue("methodInteger"));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        // Mock ArgumentResolver
        Mock mockArgumentResolver = mock(ArgumentResolver.class);
        mockArgumentResolver.expects(once())
                .method("resolve")
                .with(same(request), eq("{integer}"))
                .will(returnValue("45"));
        ArgumentResolver argumentResolver = (ArgumentResolver) mockArgumentResolver.proxy();

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();

        MethodDefinitionFinder methodDefinitionFinder = new ParanamerMethodDefinitionFinder(null, argumentResolver, new DefaultTypeConverter(), methodNameResolver, monitor);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);
        assertEquals(45, methodDefinition.getMethodArguments().get(0));
    }


    public void testMethodConvertsStringToIntegerPragmatic() throws Exception {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue("methodInteger|45"));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        // Mock ArgumentResolver
        Mock mockArgumentResolver = mock(ArgumentResolver.class);
        mockArgumentResolver.expects(once())
                .method("resolve")
                .with(same(request), eq("45"))
                .will(returnValue("45"));
        ArgumentResolver argumentResolver = (ArgumentResolver) mockArgumentResolver.proxy();

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();

        MethodDefinitionFinder methodDefinitionFinder = new ParanamerMethodDefinitionFinder(null, argumentResolver, new DefaultTypeConverter(), methodNameResolver, monitor);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);
        assertEquals(45, methodDefinition.getMethodArguments().get(0));
    }

    public void testMethodConvertsStringToFloat() throws Exception {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue("methodFloat"));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        // Mock ArgumentResolver
        Mock mockArgumentResolver = mock(ArgumentResolver.class);
        mockArgumentResolver.expects(once())
                .method("resolve")
                .with(same(request), eq("{decimal}"))
                .will(returnValue("99.99"));
        ArgumentResolver argumentResolver = (ArgumentResolver) mockArgumentResolver.proxy();

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();

        MethodDefinitionFinder methodDefinitionFinder =
                new ParanamerMethodDefinitionFinder(null, argumentResolver, new DefaultTypeConverter(), methodNameResolver, monitor);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        assertEquals(99.99f, methodDefinition.getMethodArguments().get(0));
    }

    public void testMethodConvertsStringToBoolean() throws Exception {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue("methodBoolean"));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        // Mock ArgumentResolver
        Mock mockArgumentResolver = mock(ArgumentResolver.class);
        mockArgumentResolver.expects(once())
                .method("resolve")
                .with(same(request), eq("{bool}"))
                .will(returnValue("true"));
        ArgumentResolver argumentResolver = (ArgumentResolver) mockArgumentResolver.proxy();

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();

        MethodDefinitionFinder methodDefinitionFinder =
                new ParanamerMethodDefinitionFinder(null, argumentResolver, new DefaultTypeConverter(), methodNameResolver, monitor);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        assertTrue((Boolean) methodDefinition.getMethodArguments().get(0));
    }

    public void testMethodDependsOnRequest() throws Exception {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue("methodDependsOnRequest"));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = new ParanamerMethodDefinitionFinder(null, null, null, methodNameResolver, monitor);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class
                .getMethod("methodDependsOnRequest", HttpServletRequest.class);

        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

    public void testMethodDependsOnResponse() throws Exception {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue("methodDependsOnResponse"));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = new ParanamerMethodDefinitionFinder(null, null, null, methodNameResolver, monitor);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class
                .getMethod("methodDependsOnResponse", HttpServletResponse.class);

        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

    public void testMethodDependsOnRequestAndOtherArgument() throws Exception {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue("methodDependsOnRequestAndInteger"));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        // Mock ArgumentResolver
        Mock mockArgumentResolver = mock(ArgumentResolver.class);
        mockArgumentResolver.expects(once())
                .method("resolve")
                .with(same(request), eq("{integer}"))
                .will(returnValue("99"));
        ArgumentResolver argumentResolver = (ArgumentResolver) mockArgumentResolver.proxy();

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();

        MethodDefinitionFinder methodDefinitionFinder =
                new ParanamerMethodDefinitionFinder(null, argumentResolver, new DefaultTypeConverter(), methodNameResolver, monitor);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class
                .getMethod("methodDependsOnRequestAndInteger", HttpServletRequest.class, int.class);

        assertEquals(expectedMethod, methodDefinition.getMethod());
        assertEquals(99, methodDefinition.getMethodArguments().get(1));
    }

    public void testMethodDependsOnSession() throws Exception {
        // Mock HttpSession
        Mock mockSession = mock(HttpSession.class);
        HttpSession session = (HttpSession) mockSession.proxy();

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getSession")
                .will(returnValue(session));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue("methodDependsOnSession"));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = new ParanamerMethodDefinitionFinder(null, null, null, methodNameResolver, monitor);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class
                .getMethod("methodDependsOnSession", HttpSession.class);

        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

    public void testMethodDependsOnServletContext() throws Exception {
        // Mock ServletContext
        Mock mockServletContext = mock(ServletContext.class);
        ServletContext servletContext = (ServletContext) mockServletContext.proxy();

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue("methodDependsOnServletContext"));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = new ParanamerMethodDefinitionFinder(servletContext, null, null, methodNameResolver, monitor);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class
                .getMethod("methodDependsOnServletContext", ServletContext.class);

        assertEquals(expectedMethod, methodDefinition.getMethod());
        assertSame(servletContext, methodDefinition.getMethodArguments().get(0));
    }

    public void testCustomOgnlConvertersAreLeveraged() throws Exception {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue("actionMethodNeedsCustomConverter|blah"));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        // Mock ArgumentResolver
        Mock mockArgumentResolver = mock(ArgumentResolver.class);
        mockArgumentResolver.expects(once())
                .method("resolve")
                .with(same(request), eq("blah"))
                .will(returnValue("blah"));
        ArgumentResolver argumentResolver = (ArgumentResolver) mockArgumentResolver.proxy();

        // Mock TypeConverter
        Mock mockTypeConverter = mock(TypeConverter.class);
        Constraint[] constraints = {NULL, NULL, NULL, NULL, eq("blah"), eq(List.class)};
        mockTypeConverter.expects(once())
                .method("convertValue")
                .with(constraints)
                .will(returnValue(Collections.EMPTY_LIST));
        TypeConverter typeConverter = (TypeConverter) mockTypeConverter.proxy();

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = new ParanamerMethodDefinitionFinder(null, argumentResolver, typeConverter, methodNameResolver, monitor);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class.getMethod("actionMethodNeedsCustomConverter", List.class);
        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

    public void testFindMethodWithASingleParameter() throws NoSuchMethodException {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock ArgumentResolver
        Mock mockArgumentResolver = mock(ArgumentResolver.class);
        mockArgumentResolver.expects(once())
                .method("resolve")
                .with(same(request), eq("{decimal}"))
                .will(returnValue(10f));
        ArgumentResolver argumentResolver = (ArgumentResolver) mockArgumentResolver.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodNameResolver
        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue("methodFloat"));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        SampleForMethodFinder sampleForMethodFinder = new SampleForMethodFinder();
        MethodDefinitionFinder methodDefinitionFinder = new ParanamerMethodDefinitionFinder(null, argumentResolver, null, methodNameResolver, monitor);
        MethodDefinition methodDefinition = methodDefinitionFinder.find(sampleForMethodFinder, request, response);

        Method expectedMethod = SampleForMethodFinder.class.getMethod("methodFloat", Float.class);
        assertEquals(expectedMethod, methodDefinition.getMethod());
    }

    public void doNot_testThatMethodWithNoParanamerDataIsExcepted() {
        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock ArgumentResolver
        Mock mockArgumentResolver = mock(ArgumentResolver.class);
        mockArgumentResolver.expects(once())
                .method("resolve")
                .with(same(request), eq("{decimal}"))
                .will(returnValue(10f));
        ArgumentResolver argumentResolver = (ArgumentResolver) mockArgumentResolver.proxy();

        Mock mockMethodNameResolver = mock(MethodNameResolver.class);
        mockMethodNameResolver.expects(once())
                .method("resolve")
                .with(same(request))
                .will(returnValue("doFoo"));
        MethodNameResolver methodNameResolver = (MethodNameResolver) mockMethodNameResolver.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        MethodDefinitionFinder methodDefinitionFinder = new ParanamerMethodDefinitionFinder(null, argumentResolver, null, methodNameResolver, monitor);
        try {
            methodDefinitionFinder.find(this, request, response);
            fail("should have barfed with MatchingMethodException");
        } catch (MatchingActionMethodException e) {
            // expected
        }

    }

    public void doFoo(float decimal) {

    }

    public class ControllerWithDefaultActionMethod {

        @DefaultActionMethod(parameters = {"helloworld"})
        public void foobar(String value) {

        }
    }

    public class ControllerWithDefaultActionMethodNoValue {

        @DefaultActionMethod
        public void foobar() {

        }
    }

}
