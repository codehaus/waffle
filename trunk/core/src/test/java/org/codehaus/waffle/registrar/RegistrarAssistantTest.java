/***********************************************************************************************************************
 * Copyright (C) 2005,2006 Michael Ward * All rights reserved. *
 * ------------------------------------------------------------------------- * The software in this package is published
 * under the terms of the BSD * style license a copy of which has been included with this distribution in * the
 * LICENSE.txt file. * * Original code by: Michael Ward *
 **********************************************************************************************************************/
package org.codehaus.waffle.registrar;

import static org.junit.Assert.assertNotNull;

import java.util.Hashtable;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.context.ContextLevel;
import org.codehaus.waffle.registrar.pico.PicoRegistrar;
import org.codehaus.waffle.testmodel.CustomRegistrar;
import org.junit.Test;
import org.picocontainer.defaults.DefaultPicoContainer;

public class RegistrarAssistantTest {

    @Test
    public void canExecute() {
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

    @Test(expected = WaffleException.class)
    public void cannotCreateForBadClass() {
        new RegistrarAssistant(Hashtable.class); // bad class
    }
}
