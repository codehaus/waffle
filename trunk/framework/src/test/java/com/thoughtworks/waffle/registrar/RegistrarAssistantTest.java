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
package com.thoughtworks.waffle.registrar;

import com.thoughtworks.waffle.WaffleException;
import com.thoughtworks.waffle.context.ContextLevel;
import com.thoughtworks.waffle.registrar.pico.PicoRegistrar;
import com.thoughtworks.waffle.testmodel.CustomRegistrar;
import junit.framework.TestCase;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Set;

public class RegistrarAssistantTest extends TestCase {

    @SuppressWarnings({"unchecked"})
    public void testConstructorCachesRegistrationMethods() throws Exception {
        RegistrarAssistant registrarAssistant = new RegistrarAssistant(CustomRegistrar.class);

        // Application Methods
        Field field = RegistrarAssistant.class.getDeclaredField("applicationMethods");
        field.setAccessible(true);
        Set<Method> methods = (Set<Method>) field.get(registrarAssistant);
        assertEquals(1, methods.size());
        assertEquals("application", methods.toArray(new Method[0])[0].getName());

        // Session Methods
        field = RegistrarAssistant.class.getDeclaredField("sessionMethods");
        field.setAccessible(true);
        methods = (Set<Method>) field.get(registrarAssistant);
        assertEquals(1, methods.size());
        assertEquals("session", methods.toArray(new Method[0])[0].getName());

        // Request Methods
        field = RegistrarAssistant.class.getDeclaredField("requestMethods");
        field.setAccessible(true);
        methods = (Set<Method>) field.get(registrarAssistant);
        assertEquals(1, methods.size());
        assertEquals("request", methods.toArray(new Method[0])[0].getName());
    }

    public void testExecutionSuccessful() {
        DefaultPicoContainer picoContainer = new DefaultPicoContainer();
        PicoRegistrar picoRegistrar = new PicoRegistrar(picoContainer);
        RegistrarAssistant registrarAssistant = new RegistrarAssistant(CustomRegistrar.class);

        registrarAssistant.executeDelegatingRegistrar(picoRegistrar, ContextLevel.APPLICATION);
        registrarAssistant.executeDelegatingRegistrar(picoRegistrar, ContextLevel.SESSION);
        registrarAssistant.executeDelegatingRegistrar(picoRegistrar, ContextLevel.REQUEST);

        assertNotNull(picoContainer.getComponentInstance("application"));
        assertNotNull(picoContainer.getComponentInstance("session"));
        assertNotNull(picoContainer.getComponentInstance("request"));
    }

    public void testConstructorFailsForBadClass() {
        try {
            new RegistrarAssistant(Hashtable.class); // bad class
            fail("WaffleException expected");
        } catch (WaffleException expected) {
            assertTrue("Message should display offending class name.",
                    expected.getMessage().contains(Hashtable.class.getName()));
        }
    }
}
