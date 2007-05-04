package com.thoughtworks.waffle.registrar.mock;

import org.picocontainer.MutablePicoContainer;

import com.thoughtworks.waffle.context.ContextContainer;
import com.thoughtworks.waffle.context.PicoContextContainer;
import com.thoughtworks.waffle.registrar.Registrar;
import com.thoughtworks.waffle.registrar.pico.PicoRegistrar;

/**
 * Abstract TestCase that uses PicoContainer as the ContextContainer
 * @author Mauro Talevi
 */
public abstract class AbstractPicoRegistrarTestCase extends AbstractRegistrarTestCase {

    protected ContextContainer createContextContainer() {        
        return new PicoContextContainer();
    }

    protected Registrar createRegistrar(ContextContainer container) {
        return new PicoRegistrar((MutablePicoContainer) container.getDelegate());
    }
}
