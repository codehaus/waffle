package org.codehaus.waffle.testing.registrar;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.bind.DefaultStringTransmuter;
import org.codehaus.waffle.bind.ognl.OgnlValueConverterFinder;
import org.codehaus.waffle.context.ContextLevel;
import org.codehaus.waffle.context.pico.PicoComponentRegistry;
import org.codehaus.waffle.context.pico.PicoLifecycleStrategy;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.pico.DefaultParameterResolver;
import org.codehaus.waffle.registrar.pico.PicoRegistrar;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.monitors.NullComponentMonitor;

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
        MutablePicoContainer registrarContainer = new DefaultPicoContainer();
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
            MutablePicoContainer registrarContainer) {
        Registrar registrar = createRegistrar(registrarType, registrarContainer);
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

    /**
     * Creates a registrar backed by a PicoRegistrar delegate
     * 
     * @param type the Class of the Registrar to be created
     * @param registrarContainer the container with the registrar component
     * @return The Registrar of the given type
     */
    private Registrar createRegistrar(Class<?> type, MutablePicoContainer registrarContainer) {
        String registrarName = type.getName();
        registrarContainer.addComponent(PicoComponentRegistryServletContext.class);
        try {
            MutablePicoContainer initContainer = new DefaultPicoContainer();
            initContainer.addComponent(registrarContainer);
            initContainer.addComponent(NullComponentMonitor.class);
            initContainer.addComponent(PicoLifecycleStrategy.class);
            initContainer.addComponent(SilentMonitor.class);
            initContainer.addComponent(DefaultParameterResolver.class);
            initContainer.addComponent(DefaultStringTransmuter.class);
            initContainer.addComponent(OgnlValueConverterFinder.class);
            initContainer.addComponent(PicoRegistrar.class);
            initContainer.addComponent(registrarName, Class.forName(registrarName));
            return (Registrar) initContainer.getComponent(registrarName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Registrar for " + registrarName, e);
        }
    }

    /**
     * ServletContext stub that returns a PicoComponentRegistry backed by the context itself.
     * 
     * @author Mauro Talevi
     */
    public static class PicoComponentRegistryServletContext implements ServletContext {
        
        private ComponentRegistry registry;
        
        public PicoComponentRegistryServletContext(){
            this.registry = new PicoComponentRegistry(this);
        }
        
        public Object getAttribute(String name) {
            if ( name.equals(ComponentRegistry.class.getName())){
                return registry;
            }
            return null;
        }

        public Enumeration<String> getAttributeNames() {
            return null;
        }

        public ServletContext getContext(String uripath) {
            return null;
        }

        public String getContextPath() {
            return null;
        }

        public String getInitParameter(String name) {
            return null;
        }

        public Enumeration<String> getInitParameterNames() {
            return null;
        }

        public int getMajorVersion() {
            return 0;
        }

        public String getMimeType(String file) {
            return null;
        }

        public int getMinorVersion() {
            return 0;
        }

        public RequestDispatcher getNamedDispatcher(String name) {
            return null;
        }

        public String getRealPath(String path) {
            return null;
        }

        public RequestDispatcher getRequestDispatcher(String path) {
            return null;
        }

        public URL getResource(String path) throws MalformedURLException {
            return null;
        }

        public InputStream getResourceAsStream(String path) {
            return null;
        }

        public Set<String> getResourcePaths(String path) {
            return null;
        }

        public String getServerInfo() {
            return null;
        }

        public Servlet getServlet(String name) throws ServletException {
            return null;
        }

        public String getServletContextName() {
            return null;
        }

        public Enumeration<String> getServletNames() {
            return null;
        }

        public Enumeration<?> getServlets() {
            return null;
        }

        public void log(String msg) {
        }

        public void log(Exception exception, String msg) {
        }

        public void log(String message, Throwable throwable) {
        }

        public void removeAttribute(String name) {
        }

        public void setAttribute(String name, Object object) {
        }
        
    }
}
