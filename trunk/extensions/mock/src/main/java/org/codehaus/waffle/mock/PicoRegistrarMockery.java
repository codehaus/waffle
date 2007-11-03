package org.codehaus.waffle.mock;

import org.picocontainer.MutablePicoContainer;

import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.pico.PicoContextContainer;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.pico.PicoRegistrar;

/**
 * Registrar Mockery that uses PicoContainer as the ContextContainer
 * 
 * @author Mauro Talevi
 */
public abstract class PicoRegistrarMockery extends AbstractRegistrarMockery {

    protected ContextContainer createContextContainer() {
        return new PicoContextContainer();
    }

    protected Registrar createRegistrar(ContextContainer container) {
        return new PicoRegistrar((MutablePicoContainer) container.getDelegate());
    }
}
