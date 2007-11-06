package org.codehaus.waffle.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.Constants;
import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.MethodDefinitionFinder;
import org.codehaus.waffle.context.RequestLevelContainer;
import org.codehaus.waffle.context.pico.PicoContextContainer;
import org.codehaus.waffle.testmodel.FakeController;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

/**
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class ContextControllerDefinitionFactoryTest {
    private Mockery mockery = new Mockery();

    @Test
    public void canGetControllerDefinition() throws NoSuchMethodException {
        final MutablePicoContainer pico = new DefaultPicoContainer();
        pico.registerComponentImplementation("theController", FakeController.class);
        RequestLevelContainer.set(new PicoContextContainer(pico));

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getPathInfo();
                will(returnValue("/theController.htm"));
                one(request).setAttribute(Constants.CONTROLLER_KEY,
                        pico.getComponentInstanceOfType(FakeController.class));
            }
        });

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        final MethodDefinition methodDefinition = new MethodDefinition(null);

        // Mock MethodDefinitionFinder
        final MethodDefinitionFinder finder = mockery.mock(MethodDefinitionFinder.class);
        mockery.checking(new Expectations() {
            {
                one(finder).find(with(an(FakeController.class)), with(same(request)), with(same(response)));
                will(returnValue(methodDefinition));
            }
        });

        ControllerDefinitionFactory controllerDefinitionFactory = new ContextControllerDefinitionFactory(finder,
                new ContextPathControllerNameResolver());
        ControllerDefinition controllerDefinition = controllerDefinitionFactory.getControllerDefinition(request,
                response);

        assertNotNull(controllerDefinition.getController());
        assertSame(methodDefinition, controllerDefinition.getMethodDefinition());
    }

    @Test(expected = WaffleException.class)
    public void cannotRequestControllerDefinitionThatiIsNotRegistered() {
        MutablePicoContainer pico = new DefaultPicoContainer();
        RequestLevelContainer.set(new PicoContextContainer(pico));

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getPathInfo();
                will(returnValue("/theController.htm"));
                one(request).getRequestURI();
                will(returnValue("/theController.htm"));
            }
        });

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        // Mock MethodDefinitionFinder
        final MethodDefinitionFinder finder = mockery.mock(MethodDefinitionFinder.class);

        ControllerDefinitionFactory controllerDefinitionFactory = new ContextControllerDefinitionFactory(finder,
                new ContextPathControllerNameResolver());

        controllerDefinitionFactory.getControllerDefinition(request, response);
    }

    @Test(expected = WaffleException.class)
    public void cannotGetControllerDefinitionWithMissingRequestLevelContainer() {

        RequestLevelContainer.set(null);

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);

        ContextControllerDefinitionFactory controllerDefinitionFactory = new ContextControllerDefinitionFactory(null,
                null);

        controllerDefinitionFactory.findController("foobar", request);
    }

}