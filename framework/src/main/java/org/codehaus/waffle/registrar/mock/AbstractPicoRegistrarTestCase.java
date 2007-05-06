package org.codehaus.waffle.registrar.mock;

import org.picocontainer.MutablePicoContainer;

import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.pico.PicoContextContainer;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.pico.PicoRegistrar;

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
