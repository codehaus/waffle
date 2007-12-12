/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.registrar.pico;

import org.codehaus.waffle.monitor.RegistrarMonitor;
import org.codehaus.waffle.monitor.SilentMonitor;
import static org.codehaus.waffle.registrar.InjectionType.SETTER;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.testmodel.ConstructorInjectionComponent;
import org.codehaus.waffle.testmodel.FakeBean;
import org.codehaus.waffle.testmodel.FakeController;
import org.codehaus.waffle.testmodel.SetterInjectionComponent;
import org.codehaus.waffle.context.pico.PicoLifecycleStrategy;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.monitors.NullComponentMonitor;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.picocontainer.defaults.LifecycleStrategy;

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

        Registrar registrar = new PicoRegistrar(pico, lifecycleStrategy, registrarMonitor)
                .register(type);
        assertTrue(registrar.isRegistered(type));

        FakeController controllerOne = (FakeController) pico.getComponentInstance(FakeController.class);
        FakeController controllerTwo = (FakeController) pico.getComponentInstance(FakeController.class);

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

        Registrar registrar = new PicoRegistrar(pico, lifecycleStrategy, registrarMonitor);
        registrar.register(key, type);
        assertTrue(registrar.isRegistered(type));

        FakeController controllerOne = (FakeController) pico.getComponentInstance(key);
        FakeController controllerTwo = (FakeController) pico.getComponentInstance(key);

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

        Registrar registrar = new PicoRegistrar(pico, lifecycleStrategy, registrarMonitor);
        registrar.registerInstance(fakeController);
        assertTrue(registrar.isRegistered(fakeController));

        assertSame(fakeController, pico.getComponentInstanceOfType(FakeController.class));
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

        Registrar registrar = new PicoRegistrar(pico, lifecycleStrategy, registrarMonitor);
        registrar.registerInstance(key, fakeController);
        assertTrue(registrar.isRegistered(key));

        assertSame(fakeController, pico.getComponentInstance("foobar"));
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

        Registrar registrar = new PicoRegistrar(pico, lifecycleStrategy, registrarMonitor);
        registrar.registerNonCaching(type);
        assertTrue(registrar.isRegistered(type));

        FakeController controllerOne = (FakeController) pico.getComponentInstance(FakeController.class);
        FakeController controllerTwo = (FakeController) pico.getComponentInstance(FakeController.class);

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

        Registrar registrar = new PicoRegistrar(pico, lifecycleStrategy, registrarMonitor);
        registrar.registerNonCaching(key, type);
        assertTrue(registrar.isRegistered(type));

        FakeController controllerOne = (FakeController) pico.getComponentInstance("foobar");
        FakeController controllerTwo = (FakeController) pico.getComponentInstance("foobar");

        assertNotSame(controllerOne, controllerTwo);
    }

    @Test
    public void canRegisterComponentAdapter() {
        MutablePicoContainer pico = new DefaultPicoContainer();

        ConstructorInjectionComponentAdapter componentAdapter
                = new ConstructorInjectionComponentAdapter("a", FakeController.class);

        PicoRegistrar picoRegistrar = new PicoRegistrar(pico, lifecycleStrategy, new SilentMonitor());
        picoRegistrar.registerComponentAdapter(componentAdapter);

        FakeController controllerOne = (FakeController) pico.getComponentInstance("a");
        FakeController controllerTwo = (FakeController) pico.getComponentInstanceOfType(FakeController.class);

        assertNotSame(controllerOne, controllerTwo);
    }

    @Test
    public void canSwitchInstantiationType() {
        FakeBean fakeBean = new FakeBean();
        MutablePicoContainer pico = new DefaultPicoContainer();
        PicoRegistrar picoRegistrar = new PicoRegistrar(pico, lifecycleStrategy, new SilentMonitor());

        picoRegistrar.registerInstance(fakeBean)
                .register(ConstructorInjectionComponent.class)
                .setInjectionType(SETTER)
                .register(SetterInjectionComponent.class);

        ConstructorInjectionComponent cia = (ConstructorInjectionComponent) pico.getComponentInstance(ConstructorInjectionComponent.class);
        SetterInjectionComponent sia = (SetterInjectionComponent) pico.getComponentInstance(SetterInjectionComponent.class);

        // ensure both constructed correctly
        assertSame(fakeBean, cia.getFakeBean());
        assertSame(fakeBean, sia.getFakeBean());
    }
}
