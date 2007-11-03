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

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.codehaus.waffle.testmodel.FakeController;
import org.junit.Test;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;
import org.picocontainer.defaults.DefaultPicoContainer;

public class PicoRegistrarTest {

    @Test
    public void testRegister() {
        MutablePicoContainer pico = new DefaultPicoContainer();
        PicoRegistrar picoRegistrar = new PicoRegistrar(pico);
        picoRegistrar.register(FakeController.class);

        FakeController controllerOne = (FakeController) pico.getComponentInstance(FakeController.class);
        FakeController controllerTwo = (FakeController) pico.getComponentInstance(FakeController.class);

        assertSame(controllerOne, controllerTwo);
    }

    @Test
    public void testRegisterWithKey() {
        MutablePicoContainer pico = new DefaultPicoContainer();
        PicoRegistrar picoRegistrar = new PicoRegistrar(pico);
        picoRegistrar.register("foobar", FakeController.class);

        FakeController controllerOne = (FakeController) pico.getComponentInstance("foobar");
        FakeController controllerTwo = (FakeController) pico.getComponentInstance("foobar");

        assertSame(controllerOne, controllerTwo);
        assertSame(controllerOne, pico.getComponentInstanceOfType(FakeController.class));
    }

    @Test
    public void testRegisterInstance() {
        MutablePicoContainer pico = new DefaultPicoContainer();
        PicoRegistrar picoRegistrar = new PicoRegistrar(pico);

        FakeController fakeController = new FakeController();
        picoRegistrar.registerInstance(fakeController);

        assertSame(fakeController, pico.getComponentInstanceOfType(FakeController.class));
    }

    @Test
    public void testRegisterInstanceWithKey() {
        MutablePicoContainer pico = new DefaultPicoContainer();
        PicoRegistrar picoRegistrar = new PicoRegistrar(pico);

        FakeController fakeController = new FakeController();
        picoRegistrar.registerInstance("foobar", fakeController);

        assertSame(fakeController, pico.getComponentInstance("foobar"));
        assertSame(fakeController, pico.getComponentInstanceOfType(FakeController.class));
    }

    @Test
    public void testNonCachingRegistration() {
        MutablePicoContainer pico = new DefaultPicoContainer();
        PicoRegistrar picoRegistrar = new PicoRegistrar(pico);

        picoRegistrar.registerNonCaching(FakeController.class);

        FakeController controllerOne = (FakeController) pico.getComponentInstance(FakeController.class);
        FakeController controllerTwo = (FakeController) pico.getComponentInstance(FakeController.class);

        assertNotSame(controllerOne, controllerTwo);
    }

    @Test
    public void testNonCachingRegistrationWithKey() {
        MutablePicoContainer pico = new DefaultPicoContainer();
        PicoRegistrar picoRegistrar = new PicoRegistrar(pico);

        picoRegistrar.registerNonCaching("foobar", FakeController.class);

        FakeController controllerOne = (FakeController) pico.getComponentInstance("foobar");
        FakeController controllerTwo = (FakeController) pico.getComponentInstance("foobar");

        assertNotSame(controllerOne, controllerTwo);
    }

    @Test
    public void testRegisterComponentAdapter() {
        MutablePicoContainer pico = new DefaultPicoContainer();
        PicoRegistrar picoRegistrar = new PicoRegistrar(pico);

        ConstructorInjectionComponentAdapter componentAdapter
                = new ConstructorInjectionComponentAdapter("a", FakeController.class);

        picoRegistrar.registerComponentAdapter(componentAdapter);

        FakeController controllerOne = (FakeController) pico.getComponentInstance("a");
        FakeController controllerTwo = (FakeController) pico.getComponentInstanceOfType(FakeController.class);

        assertNotSame(controllerOne, controllerTwo);
    }
}
