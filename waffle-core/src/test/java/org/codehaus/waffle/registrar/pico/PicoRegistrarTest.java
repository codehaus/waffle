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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.codehaus.waffle.context.pico.PicoLifecycleStrategy;
import org.codehaus.waffle.monitor.RegistrarMonitor;
import org.codehaus.waffle.monitor.SilentMonitor;
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
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.picocontainer.defaults.LifecycleStrategy;
import org.picocontainer.monitors.NullComponentMonitor;

/**
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class PicoRegistrarTest {
    private LifecycleStrategy lifecycleStrategy = new PicoLifecycleStrategy(new NullComponentMonitor());

    private Mockery mockery = new Mockery();

    @Test
    public void canRegisterComponent() {

        MutablePicoContainer pico = new DefaultPicoContainer();

        // Mock RegistrarMonitor
        final RegistrarMonitor registrarMonitor = mockery.mock(RegistrarMonitor.class);
        final Class<?> type = FakeController.class;
        mockery.checking(new Expectations() {
            {
                one(registrarMonitor).componentRegistered(type, type, new Object[]{});
            }
        });

        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, registrarMonitor)
                .register(type);
        assertTrue(registrar.isRegistered(type));

        FakeController controllerOne = (FakeController) registrar.getRegistered(FakeController.class);
        FakeController controllerTwo = (FakeController) registrar.getRegistered(FakeController.class);

        assertSame(controllerOne, controllerTwo);
    }

    @Test
    public void canRegisterComponentWithKey() {
        MutablePicoContainer pico = new DefaultPicoContainer();
        // Mock RegistrarMonitor
        final RegistrarMonitor registrarMonitor = mockery.mock(RegistrarMonitor.class);
        final String key = "foobar";
        final Class<?> type = FakeController.class;
        mockery.checking(new Expectations() {
            {
                one(registrarMonitor).componentRegistered(key, type, new Object[]{});
            }
        });

        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, registrarMonitor);
        registrar.register(key, type);
        assertTrue(registrar.isRegistered(type));

        FakeController controllerOne = (FakeController) registrar.getRegistered(key);
        FakeController controllerTwo = (FakeController) registrar.getRegistered(key);

        assertSame(controllerOne, controllerTwo);
        assertSame(controllerOne, pico.getComponentInstanceOfType(type));
    }

    @Test
    public void canRegisterInstance() {
        MutablePicoContainer pico = new DefaultPicoContainer();
        // Mock RegistrarMonitor
        final RegistrarMonitor registrarMonitor = mockery.mock(RegistrarMonitor.class);
        final FakeController fakeController = new FakeController();
        mockery.checking(new Expectations() {
            {
                one(registrarMonitor).instanceRegistered(fakeController, fakeController);
            }
        });

        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, registrarMonitor);
        registrar.registerInstance(fakeController);
        assertTrue(registrar.isRegistered(fakeController));

        assertSame(fakeController, registrar.getRegistered(FakeController.class));
    }

    @Test
    public void canRegisterInstanceWithKey() {
        MutablePicoContainer pico = new DefaultPicoContainer();
        // Mock RegistrarMonitor
        final RegistrarMonitor registrarMonitor = mockery.mock(RegistrarMonitor.class);
        final String key = "foobar";
        final FakeController fakeController = new FakeController();
        mockery.checking(new Expectations() {
            {
                one(registrarMonitor).instanceRegistered(key, fakeController);
            }
        });

        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, registrarMonitor);
        registrar.registerInstance(key, fakeController);
        assertTrue(registrar.isRegistered(key));

        assertSame(fakeController, registrar.getRegistered("foobar"));
        assertSame(fakeController, pico.getComponentInstanceOfType(FakeController.class));
    }

    @Test
    public void canRegisterNonCachingComponent() {
        MutablePicoContainer pico = new DefaultPicoContainer();
        // Mock RegistrarMonitor
        final RegistrarMonitor registrarMonitor = mockery.mock(RegistrarMonitor.class);
        final Class<?> type = FakeController.class;
        mockery.checking(new Expectations() {
            {
                one(registrarMonitor).nonCachingComponentRegistered(type, type, new Object[]{});
            }
        });

        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, registrarMonitor);
        registrar.registerNonCaching(type);
        assertTrue(registrar.isRegistered(type));

        FakeController controllerOne = (FakeController) registrar.getRegistered(FakeController.class);
        FakeController controllerTwo = (FakeController) registrar.getRegistered(FakeController.class);

        assertNotSame(controllerOne, controllerTwo);
    }

    @Test
    public void canRegisterNonCachingComponentWithKey() {
        MutablePicoContainer pico = new DefaultPicoContainer();
        // Mock RegistrarMonitor
        final RegistrarMonitor registrarMonitor = mockery.mock(RegistrarMonitor.class);
        final String key = "foobar";
        final Class<?> type = FakeController.class;
        mockery.checking(new Expectations() {
            {
                one(registrarMonitor).nonCachingComponentRegistered(key, type, new Object[]{});
            }
        });

        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, registrarMonitor);
        registrar.registerNonCaching(key, type);
        assertTrue(registrar.isRegistered(type));

        FakeController controllerOne = (FakeController) registrar.getRegistered("foobar");
        FakeController controllerTwo = (FakeController) registrar.getRegistered("foobar");

        assertNotSame(controllerOne, controllerTwo);
    }

    @Test
    public void canSwitchInjectionType() {
        FakeBean fakeBean = new FakeBean();
        MutablePicoContainer pico = new DefaultPicoContainer();
        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, new SilentMonitor());

        registrar.registerInstance(fakeBean)
                .register(ConstructorInjectionComponent.class)
                .useInjection(Registrar.Injection.SETTER)
                .register(SetterInjectionComponent.class);

        ConstructorInjectionComponent cia = (ConstructorInjectionComponent) pico.getComponentInstance(ConstructorInjectionComponent.class);
        SetterInjectionComponent sia = (SetterInjectionComponent) pico.getComponentInstance(SetterInjectionComponent.class);

        // ensure both constructed correctly
        assertSame(fakeBean, cia.getFakeBean());
        assertSame(fakeBean, sia.getFakeBean());
    }

    @Test
    public void canRegisterComponentWithConstantParameters() {
        MutablePicoContainer pico = new DefaultPicoContainer();
        ParameterResolver parameterResolver = new DefaultParameterResolver(null);
        Registrar registrar = new PicoRegistrar(pico, parameterResolver, lifecycleStrategy, new SilentMonitor());

        registrar.register("component", ComponentWithParameterDependencies.class, "foo", "bar");

        ComponentWithParameterDependencies component = (ComponentWithParameterDependencies) registrar.getRegistered("component");

        // ensure both constructed correctly
        assertSame("foo", component.getValueOne());
        assertSame("bar", component.getValueTwo());
    }

    @Test
    public void canRegisterComponentWithNamedDependency() {
        MutablePicoContainer pico = new DefaultPicoContainer();
        ParameterResolver parameterResolver = new DefaultParameterResolver(null);
        Registrar registrar = new PicoRegistrar(pico, parameterResolver, lifecycleStrategy, new SilentMonitor());

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
        MutablePicoContainer pico = new DefaultPicoContainer();

        ConstructorInjectionComponentAdapter componentAdapter
                = new ConstructorInjectionComponentAdapter("a", FakeController.class);

        PicoRegistrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, new SilentMonitor());
        registrar.registerComponentAdapter(componentAdapter);

        FakeController controllerOne = (FakeController) registrar.getRegistered("a");
        FakeController controllerTwo = (FakeController) pico.getComponentInstanceOfType(FakeController.class);

        assertNotSame(controllerOne, controllerTwo);
    }

    @Test(expected=RegistrarException.class)
    public void cannotGetRegistedComponentWithUnknownKey() {

        MutablePicoContainer pico = new DefaultPicoContainer();

        // Mock RegistrarMonitor
        final RegistrarMonitor registrarMonitor = mockery.mock(RegistrarMonitor.class);
        final Class<?> type = FakeController.class;
        mockery.checking(new Expectations() {
            {
                one(registrarMonitor).componentRegistered(type, type, new Object[]{});
            }
        });

        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, registrarMonitor)
                .register(type);
        assertTrue(registrar.isRegistered(type));
        assertFalse(registrar.isRegistered("unknownKey"));
        registrar.getRegistered("unknownKey");
    }

    @Test(expected=RegistrarException.class)
    public void cannotGetRegistedComponentThatHasNotBeenRegistered() {

        MutablePicoContainer pico = new DefaultPicoContainer();

        // Mock RegistrarMonitor
        final RegistrarMonitor registrarMonitor = mockery.mock(RegistrarMonitor.class);
        final Class<?> type = FakeController.class;
        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, registrarMonitor);
        assertFalse(registrar.isRegistered(type));
        registrar.getRegistered(type);
    }

    @Test(expected=RegistrarException.class)
    public void cannotGetRegistedComponentThatHasMultipleInstancesRegistered() {

        MutablePicoContainer pico = new DefaultPicoContainer();

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
        Registrar registrar = new PicoRegistrar(pico, null, lifecycleStrategy, registrarMonitor);
        registrar.register(type);
        registrar.register(subType);
        assertTrue(registrar.isRegistered(type));
        registrar.getRegistered(type);
    }

    public static final class SubFakeController extends FakeController {
        
    }
}
