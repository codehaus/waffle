/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.registrar.pico;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javax.servlet.ServletContext;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.context.pico.PicoLifecycleStrategy;
import org.codehaus.waffle.monitor.RegistrarMonitor;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.ComponentReference;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.RegistrarException;
import org.codehaus.waffle.testmodel.ComponentWithParameterDependencies;
import org.codehaus.waffle.testmodel.ConstructorInjectionComponent;
import org.codehaus.waffle.testmodel.FakeBean;
import org.codehaus.waffle.testmodel.FakeController;
import org.codehaus.waffle.testmodel.SetterInjectionComponent;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.LifecycleStrategy;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.containers.EmptyPicoContainer;
import org.picocontainer.lifecycle.NullLifecycleStrategy;
import org.picocontainer.behaviors.Caching;
import org.picocontainer.injectors.ConstructorInjector;
import org.picocontainer.monitors.NullComponentMonitor;

/**
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class PicoRegistrarTest {
    private LifecycleStrategy lifecycleStrategy = new PicoLifecycleStrategy(new NullComponentMonitor());

    private Mockery mockery = new Mockery();
    ComponentMonitor cm = new NullComponentMonitor();

    @Test
    public void canRegisterComponent() {

        MutablePicoContainer pico = new DefaultPicoContainer(new Caching(), new NullLifecycleStrategy(), new EmptyPicoContainer(), cm);

        // Mock RegistrarMonitor
        final RegistrarMonitor registrarMonitor = mockery.mock(RegistrarMonitor.class);
        final Class<?> type = FakeController.class;
        mockery.checking(new Expectations() {
            {
                one(registrarMonitor).componentRegistered(type, type, new Object[]{});
            }
        });

        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, registrarMonitor, cm)
                .register(type);
        assertTrue(registrar.isRegistered(type));

        FakeController controllerOne = (FakeController) registrar.getRegistered(FakeController.class);
        FakeController controllerTwo = (FakeController) registrar.getRegistered(FakeController.class);

        assertSame(controllerOne, controllerTwo);
    }

    @Test
    public void canRegisterComponentWithKey() {
        MutablePicoContainer pico = new DefaultPicoContainer(new Caching(), new NullLifecycleStrategy(), new EmptyPicoContainer(), cm);
        // Mock RegistrarMonitor
        final RegistrarMonitor registrarMonitor = mockery.mock(RegistrarMonitor.class);
        final String key = "foobar";
        final Class<?> type = FakeController.class;
        mockery.checking(new Expectations() {
            {
                one(registrarMonitor).componentRegistered(key, type, new Object[]{});
            }
        });

        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, registrarMonitor, cm);
        registrar.register(key, type);
        assertTrue(registrar.isRegistered(type));

        FakeController controllerOne = (FakeController) registrar.getRegistered(key);
        FakeController controllerTwo = (FakeController) registrar.getRegistered(key);

        assertSame(controllerOne, controllerTwo);
        assertSame(controllerOne, pico.getComponent(type));
    }

    @Test
    public void canRegisterInstance() {
        MutablePicoContainer pico = new DefaultPicoContainer(new Caching(), new NullLifecycleStrategy(), new EmptyPicoContainer(), cm);
        // Mock RegistrarMonitor
        final RegistrarMonitor registrarMonitor = mockery.mock(RegistrarMonitor.class);
        final FakeController fakeController = new FakeController();
        mockery.checking(new Expectations() {
            {
                one(registrarMonitor).instanceRegistered(fakeController, fakeController);
            }
        });

        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, registrarMonitor, cm);
        registrar.registerInstance(fakeController);
        assertTrue(registrar.isRegistered(fakeController));

        assertSame(fakeController, registrar.getRegistered(FakeController.class));
    }

    @Test
    public void canRegisterInstanceWithKey() {
        MutablePicoContainer pico = new DefaultPicoContainer(new Caching(), new NullLifecycleStrategy(), new EmptyPicoContainer(), cm);
        // Mock RegistrarMonitor
        final RegistrarMonitor registrarMonitor = mockery.mock(RegistrarMonitor.class);
        final String key = "foobar";
        final FakeController fakeController = new FakeController();
        mockery.checking(new Expectations() {
            {
                one(registrarMonitor).instanceRegistered(key, fakeController);
            }
        });

        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, registrarMonitor, cm);
        registrar.registerInstance(key, fakeController);
        assertTrue(registrar.isRegistered(key));

        assertSame(fakeController, registrar.getRegistered("foobar"));
        assertSame(fakeController, pico.getComponent(FakeController.class));
    }

    @Test
    public void canRegisterNonCachingComponent() {
        MutablePicoContainer pico = new DefaultPicoContainer(new Caching(), new NullLifecycleStrategy(), new EmptyPicoContainer(), cm);
        // Mock RegistrarMonitor
        final RegistrarMonitor registrarMonitor = mockery.mock(RegistrarMonitor.class);
        final Class<?> type = FakeController.class;
        mockery.checking(new Expectations() {
            {
                one(registrarMonitor).nonCachingComponentRegistered(type, type, new Object[]{});
            }
        });

        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, registrarMonitor, cm);
        registrar.registerNonCaching(type);
        assertTrue(registrar.isRegistered(type));

        FakeController controllerOne = (FakeController) registrar.getRegistered(FakeController.class);
        FakeController controllerTwo = (FakeController) registrar.getRegistered(FakeController.class);

        assertNotSame(controllerOne, controllerTwo);
    }

    @Test
    public void canRegisterNonCachingComponentWithKey() {
        MutablePicoContainer pico = new DefaultPicoContainer(new Caching(), new NullLifecycleStrategy(), new EmptyPicoContainer(), cm);
        // Mock RegistrarMonitor
        final RegistrarMonitor registrarMonitor = mockery.mock(RegistrarMonitor.class);
        final String key = "foobar";
        final Class<?> type = FakeController.class;
        mockery.checking(new Expectations() {
            {
                one(registrarMonitor).nonCachingComponentRegistered(key, type, new Object[]{});
            }
        });

        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, registrarMonitor, cm);
        registrar.registerNonCaching(key, type);
        assertTrue(registrar.isRegistered(type));

        FakeController controllerOne = (FakeController) registrar.getRegistered("foobar");
        FakeController controllerTwo = (FakeController) registrar.getRegistered("foobar");

        assertNotSame(controllerOne, controllerTwo);
    }

    @Test
    public void canSwitchInjectionType() {
        FakeBean fakeBean = new FakeBean();
        MutablePicoContainer pico = new DefaultPicoContainer(new Caching(), new NullLifecycleStrategy(), new EmptyPicoContainer(), cm);
        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, new SilentMonitor(), cm);

        registrar.registerInstance(fakeBean)
                .register(ConstructorInjectionComponent.class)
                .useInjection(Registrar.Injection.SETTER)
                .register(SetterInjectionComponent.class);

        ConstructorInjectionComponent cia = (ConstructorInjectionComponent) pico.getComponent(ConstructorInjectionComponent.class);
        SetterInjectionComponent sia = (SetterInjectionComponent) pico.getComponent(SetterInjectionComponent.class);

        // ensure both constructed correctly
        assertSame(fakeBean, cia.getFakeBean());
        assertSame(fakeBean, sia.getFakeBean());
    }

    @Test
    public void canRegisterComponentWithConstantParameters() {
        MutablePicoContainer pico = new DefaultPicoContainer(new Caching(), new NullLifecycleStrategy(), new EmptyPicoContainer(), cm);
        ParameterResolver parameterResolver = new DefaultParameterResolver(null);
        Registrar registrar = new PicoRegistrar(pico, parameterResolver, lifecycleStrategy, new SilentMonitor(), cm);

        registrar.register("component", ComponentWithParameterDependencies.class, "foo", "bar");

        ComponentWithParameterDependencies component = (ComponentWithParameterDependencies) registrar.getRegistered("component");

        // ensure both constructed correctly
        assertSame("foo", component.getValueOne());
        assertSame("bar", component.getValueTwo());
    }

    @Test
    public void canRegisterComponentWithNamedDependency() {
        MutablePicoContainer pico = new DefaultPicoContainer(new Caching(), new NullLifecycleStrategy(), new EmptyPicoContainer(), cm);
        ParameterResolver parameterResolver = new DefaultParameterResolver(null);
        Registrar registrar = new PicoRegistrar(pico, parameterResolver, lifecycleStrategy, new SilentMonitor(), cm);

        registrar.registerInstance("one", "foo")
                .registerInstance("two", "bar")
                .register("component", ComponentWithParameterDependencies.class, new ComponentReference("one"), new ComponentReference("two"));

        ComponentWithParameterDependencies component = (ComponentWithParameterDependencies) registrar.getRegistered("component");

        // ensure both constructed correctly
        assertSame("foo", component.getValueOne());
        assertSame("bar", component.getValueTwo());
    }

    @Test
    public void canRegisterComponentAdapter() {
        MutablePicoContainer pico = new DefaultPicoContainer(new Caching(), new NullLifecycleStrategy(), new EmptyPicoContainer(), cm);

        ConstructorInjector<FakeController> componentAdapter
                = new ConstructorInjector<FakeController>("a", FakeController.class);

        PicoRegistrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, new SilentMonitor(), cm);
        registrar.registerComponentAdapter(componentAdapter);

        FakeController controllerOne = (FakeController) registrar.getRegistered("a");
        FakeController controllerTwo = (FakeController) pico.getComponent(FakeController.class);

        assertNotSame(controllerOne, controllerTwo);
    }
    

    @Test
    public void canGetComponentRegistryFromDecorator() {
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        final ComponentRegistry componentRegistry = mockery.mock(ComponentRegistry.class);
        mockery.checking(new Expectations(){{
            one(servletContext).getAttribute(ComponentRegistry.class.getName());
            will(returnValue(componentRegistry));
        }});
        
        MutablePicoContainer pico = new DefaultPicoContainer(new Caching(), new NullLifecycleStrategy(), new EmptyPicoContainer(), cm);
        pico.addComponent(servletContext);
        PicoRegistrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, new SilentMonitor(), cm);
        DecoratorRegistrar decorator = new DecoratorRegistrar(registrar);
        decorator.application();
        assertNotNull(decorator.registry);
        
    }

    static class DecoratorRegistrar extends AbstractRegistrar {
        private ComponentRegistry registry;

        public DecoratorRegistrar(Registrar delegate) {
            super(delegate);
        }

        public void application(){
            registry = getComponentRegistry();
        }
        
    }
    @Test(expected=RegistrarException.class)
    public void cannotGetRegistedComponentWithUnknownKey() {

        MutablePicoContainer pico = new DefaultPicoContainer(new Caching(), new NullLifecycleStrategy(), new EmptyPicoContainer(), cm);

        // Mock RegistrarMonitor
        final RegistrarMonitor registrarMonitor = mockery.mock(RegistrarMonitor.class);
        final Class<?> type = FakeController.class;
        mockery.checking(new Expectations() {
            {
                one(registrarMonitor).componentRegistered(type, type, new Object[]{});
            }
        });

        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, registrarMonitor, cm)
                .register(type);
        assertTrue(registrar.isRegistered(type));
        assertFalse(registrar.isRegistered("unknownKey"));
        registrar.getRegistered("unknownKey");
    }

    @Test(expected=RegistrarException.class)
    public void cannotGetRegistedComponentThatHasNotBeenRegistered() {

        MutablePicoContainer pico = new DefaultPicoContainer(new Caching(), new NullLifecycleStrategy(), new EmptyPicoContainer(), cm);

        // Mock RegistrarMonitor
        final RegistrarMonitor registrarMonitor = mockery.mock(RegistrarMonitor.class);
        final Class<?> type = FakeController.class;
        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, registrarMonitor, cm);
        assertFalse(registrar.isRegistered(type));
        registrar.getRegistered(type);
    }

    @Test(expected=RegistrarException.class)
    public void cannotGetRegistedComponentThatHasMultipleInstancesRegistered() {

        MutablePicoContainer pico = new DefaultPicoContainer(new Caching(), new NullLifecycleStrategy(), new EmptyPicoContainer(), cm);

        // Mock RegistrarMonitor
        final RegistrarMonitor registrarMonitor = mockery.mock(RegistrarMonitor.class);
        final Class<?> type = FakeController.class;
        final Class<?> subType = SubFakeController.class;
        mockery.checking(new Expectations() {
            {
                one(registrarMonitor).componentRegistered(type, type, new Object[]{});
                one(registrarMonitor).componentRegistered(subType, subType, new Object[]{});
            }
        });
        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, registrarMonitor, cm);
        registrar.register(type);
        registrar.register(subType);
        assertTrue(registrar.isRegistered(type));
        registrar.getRegistered(type);
    }

    public static final class SubFakeController extends FakeController {
        
    }
}
