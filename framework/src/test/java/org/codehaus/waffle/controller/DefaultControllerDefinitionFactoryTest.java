package org.codehaus.waffle.controller;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.Constants;
import org.codehaus.waffle.controller.DefaultControllerDefinitionFactory;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;
import org.codehaus.waffle.controller.DefaultControllerNameResolver;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.action.method.MethodDefinition;
import org.codehaus.waffle.action.method.MethodDefinitionFinder;
import org.codehaus.waffle.context.PicoContextContainer;
import org.codehaus.waffle.context.RequestLevelContainer;
import org.codehaus.waffle.testmodel.FakeController;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultControllerDefinitionFactoryTest extends MockObjectTestCase {

    public void testCanGetControllerDefinition() throws NoSuchMethodException {
        MutablePicoContainer pico = new DefaultPicoContainer();
        pico.registerComponentImplementation("theController", FakeController.class);
        RequestLevelContainer.set(new PicoContextContainer(pico));

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getPathInfo")
                .will(returnValue("/theController.htm"));
        mockRequest.expects(once()).method("setAttribute")
                .with(eq(Constants.CONTROLLER_KEY), isA(FakeController.class));
        HttpServletRequest httpRequest = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        MethodDefinition methodDefinition = new MethodDefinition(null);

        // Mock MethodDefinitionFinder
        Mock mockMethodDefinitionFinder = mock(MethodDefinitionFinder.class);
        mockMethodDefinitionFinder.expects(once())
                .method("find")
                .with(isA(FakeController.class), same(httpRequest), same(response))
                .will(returnValue(methodDefinition));
        MethodDefinitionFinder methodDefinitionFinder = (MethodDefinitionFinder) mockMethodDefinitionFinder.proxy();

        ControllerDefinitionFactory controllerDefinitionFactory
                = new DefaultControllerDefinitionFactory(methodDefinitionFinder, new DefaultControllerNameResolver());
        ControllerDefinition controllerDefinition = controllerDefinitionFactory.getControllerDefinition(httpRequest, response);

        assertNotNull(controllerDefinition.getController());
        assertSame(methodDefinition, controllerDefinition.getMethodDefinition());
    }

    public void testRequestingControllerDefinitionThatiIsNotRegisteredThrowsException() throws NoSuchMethodException {
        MutablePicoContainer pico = new DefaultPicoContainer();
        RequestLevelContainer.set(new PicoContextContainer(pico));

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once())
                .method("getPathInfo")
                .will(returnValue("/theController.htm"));
        mockRequest.expects(once())
                .method("getRequestURI")
                .will(returnValue("/theController.htm"));
        HttpServletRequest httpRequest = (HttpServletRequest) mockRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        // Mock MethodDefinitionFinder
        Mock mockMethodDefinitionFinder = mock(MethodDefinitionFinder.class);
        MethodDefinitionFinder methodDefinitionFinder = (MethodDefinitionFinder) mockMethodDefinitionFinder.proxy();

        ControllerDefinitionFactory controllerDefinitionFactory
                = new DefaultControllerDefinitionFactory(methodDefinitionFinder, new DefaultControllerNameResolver());

        try {
            controllerDefinitionFactory.getControllerDefinition(httpRequest, response);
            fail("WaffleException expected");
        } catch (WaffleException e) {
            //expected
        }        
    }

    
    public void testMissingRequestLevelContainerThrowsException() throws NoSuchMethodException {
        RequestLevelContainer.set(null);
        // Mock HttpServletRequest
        Mock mockHttpServletRequest = mock(HttpServletRequest.class);
        HttpServletRequest httpRequest = (HttpServletRequest) mockHttpServletRequest.proxy();

        // Mock HttpServletResponse
        Mock mockResponse = mock(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) mockResponse.proxy();

        ControllerDefinitionFactory controllerDefinitionFactory = new DefaultControllerDefinitionFactory(null, null);

        try {
            controllerDefinitionFactory.getControllerDefinition(httpRequest, response).getController();
            fail("WaffleException should have been thrown when no request level container exists");
        } catch (WaffleException expected) {
            // expected
        }
    }

}
