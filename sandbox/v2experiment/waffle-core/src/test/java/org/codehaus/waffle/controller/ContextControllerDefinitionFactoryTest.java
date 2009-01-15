package org.codehaus.waffle.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.Constants;
import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.MethodDefinitionFinder;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.i18n.MessagesContext;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.testmodel.FakeController;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.behaviors.Caching;

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
        final MutablePicoContainer pico = new DefaultPicoContainer(new Caching());
        pico.addComponent("theController", FakeController.class);

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        final MessagesContext messagesContext = mockery.mock(MessagesContext.class);
        mockery.checking(new Expectations() {
            {
                one(request).getPathInfo();
                will(returnValue("/theController.htm"));
                one(request).setAttribute(Constants.CONTROLLER_KEY,
                        pico.getComponent(FakeController.class));
            }
        });

        // Mock HttpServletResponse
        final HttpServletResponse response = mockery.mock(HttpServletResponse.class);

        final MethodDefinition methodDefinition = new MethodDefinition(null);

        // Mock MethodDefinitionFinder
        final MethodDefinitionFinder finder = mockery.mock(MethodDefinitionFinder.class);
        mockery.checking(new Expectations() {
            {
                one(finder).find(with(an(FakeController.class)), with(same(request)), with(same(response)), with(same(messagesContext)));
                will(returnValue(methodDefinition));
            }
        });

        ControllerDefinitionFactory controllerDefinitionFactory = new ContextControllerDefinitionFactory(finder,
                new ContextPathControllerNameResolver(new SilentMonitor()), new SilentMonitor(), new DefaultMessageResources());
        ControllerDefinition controllerDefinition = controllerDefinitionFactory.getControllerDefinition(request,
                response, messagesContext, pico);

        assertNotNull(controllerDefinition.getController());
        assertSame(methodDefinition, controllerDefinition.getMethodDefinition());
    }

    @Test(expected = WaffleException.class)
    public void cannotRequestControllerDefinitionThatiIsNotRegistered() {
        MutablePicoContainer pico = new DefaultPicoContainer(new Caching());

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        MessagesContext context = mockery.mock(MessagesContext.class);

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
                new ContextPathControllerNameResolver(new SilentMonitor()), new SilentMonitor(), new DefaultMessageResources());

        ControllerDefinition definition = controllerDefinitionFactory.getControllerDefinition(request, response, context, pico);
    }

}
