package org.codehaus.waffle.testing.registrar;

import org.codehaus.waffle.bind.DefaultStringTransmuter;
import org.codehaus.waffle.bind.ognl.OgnlValueConverterFinder;
import org.codehaus.waffle.context.ContextLevel;
import org.codehaus.waffle.context.pico.PicoLifecycleStrategy;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.pico.DefaultParameterResolver;
import org.codehaus.waffle.registrar.pico.PicoRegistrar;
import org.picocontainer.monitors.NullComponentMonitor;
import org.picocontainer.DefaultPicoContainer;

/**
 * Registrar helper class. Retrieves controller instances registered in a Registrar and allows the registration of all
 * the components in a given context level.
 * 
 * @author Mauro Talevi
 */
public class RegistrarHelper {

    /**
     * Returns the registered controller
     * 
     * @param registrarType the Class representing the registrar type
     * @param level the ContextLevel
     * @param path the path under which the controller is registered
     * @return The controller instance or <code>null</code> if not found
     */
    public Object controllerFor(Class<?> registrarType, ContextLevel level, String path) {
        DefaultPicoContainer registrarContainer = new DefaultPicoContainer();
        registerComponentsFor(registrarType, level, registrarContainer);
        return registrarContainer.getComponent(path);
    }

    /**
     * Registers the components for the given registrar and level
     * 
     * @param registrarType the Class representing the registrar type
     * @param level the ContextLevel
     */
    public void componentsFor(Class<?> registrarType, ContextLevel level) {
        registerComponentsFor(registrarType, level, new DefaultPicoContainer());
    }

    private void registerComponentsFor(Class<?> registrarType, ContextLevel level,
            DefaultPicoContainer registrarContainer) {
        Registrar registrar = createRegistrar(registrarContainer, registrarType);
        switch (level) {
            case APPLICATION:
                registrar.application();
                break;
            case SESSION:
                registrar.session();
                break;
            case REQUEST:
                registrar.request();
                break;
        }
    }

    private Registrar createRegistrar(DefaultPicoContainer container, Class<?> type) {
        String name = type.getName();
        try {
            DefaultPicoContainer initContainer = new DefaultPicoContainer();
            initContainer.addComponent(container);
            initContainer.addComponent(NullComponentMonitor.class);
            initContainer.addComponent(PicoLifecycleStrategy.class);
            initContainer.addComponent(SilentMonitor.class);
            initContainer.addComponent(PicoRegistrar.class);
            initContainer.addComponent(DefaultParameterResolver.class);
            initContainer.addComponent(DefaultStringTransmuter.class);
            initContainer.addComponent(OgnlValueConverterFinder.class);
            initContainer.addComponent(name, Class.forName(name));
            return (Registrar) initContainer.getComponent(name);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Registrar for " + name, e);
        }
    }

}
