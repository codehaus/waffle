package org.codehaus.waffle.mock;

import org.codehaus.waffle.bind.DefaultStringTransmuter;
import org.codehaus.waffle.bind.ognl.OgnlValueConverterFinder;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.pico.PicoContextContainer;
import org.codehaus.waffle.context.pico.PicoLifecycleStrategy;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.pico.DefaultParameterResolver;
import org.codehaus.waffle.registrar.pico.ParameterResolver;
import org.codehaus.waffle.registrar.pico.PicoRegistrar;
import org.picocontainer.LifecycleStrategy;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.monitors.NullComponentMonitor;

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
        DefaultMessageResources messageResources = new DefaultMessageResources();
        ParameterResolver parameterResolver = new DefaultParameterResolver(new DefaultStringTransmuter(
                new OgnlValueConverterFinder()), messageResources);
        return new PicoRegistrar((MutablePicoContainer) container.getDelegate(), parameterResolver, lifecycleStrategy,
                getRegistrarMonitor(container), new NullComponentMonitor(), messageResources);
    }
}
