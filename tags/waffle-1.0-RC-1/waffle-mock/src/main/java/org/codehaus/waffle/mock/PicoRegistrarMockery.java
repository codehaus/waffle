package org.codehaus.waffle.mock;

import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.pico.PicoContextContainer;
import org.codehaus.waffle.context.pico.PicoLifecycleStrategy;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.pico.PicoRegistrar;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.monitors.NullComponentMonitor;
import org.picocontainer.defaults.LifecycleStrategy;

/**
 * Registrar Mockery that uses PicoContainer as the ContextContainer
 * 
 * @author Mauro Talevi
 */
public class PicoRegistrarMockery extends AbstractRegistrarMockery {

    protected ContextContainer createContextContainer() {
        return new PicoContextContainer();
    }

    protected Registrar createRegistrar(ContextContainer container) {
        LifecycleStrategy lifecycleStrategy = new PicoLifecycleStrategy(new NullComponentMonitor());
        return new PicoRegistrar((MutablePicoContainer) container.getDelegate(), lifecycleStrategy, getRegistrarMonitor(container));
    }
}
