package org.codehaus.waffle.testing.registrar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.bind.DefaultStringTransmuter;
import org.codehaus.waffle.bind.ognl.OgnlValueConverterFinder;
import org.codehaus.waffle.context.ContextLevel;
import org.codehaus.waffle.context.pico.PicoLifecycleStrategy;
import org.codehaus.waffle.i18n.DefaultMessageResources;
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

    private MutablePicoContainer applicationContainer;
    private MutablePicoContainer sessionContainer;
    private MutablePicoContainer requestContainer;

    /**
     * Registers the components for the given registrar and level
     * 
     * @param registrarType the Class representing the registrar type
     * @param level the ContextLevel
     */
    public void componentsFor(Class<?> registrarType, ContextLevel level) {
        registerComponentsFor(registrarType, level, registrarContainerFor(level));
    }

    /**
     * Returns the registered controller
     * 
     * @param registrarType the Class representing the registrar type
     * @param level the ContextLevel
     * @param path the path under which the controller is registered
     * @return The controller instance or <code>null</code> if not found
     */
    public Object controllerFor(Class<?> registrarType, ContextLevel level, String path) {
        MutablePicoContainer registrarContainer = registrarContainerFor(level);
        registerComponentsFor(registrarType, level, registrarContainer);
        return registrarContainer.getComponent(path);
    }

    private MutablePicoContainer registrarContainerFor(ContextLevel level) {
        switch (level) {
            case APPLICATION:
                applicationContainer = new DefaultPicoContainer();
                return applicationContainer;
            case SESSION:
                sessionContainer = new DefaultPicoContainer(applicationContainer);
                return sessionContainer;
            case REQUEST:
                requestContainer = new DefaultPicoContainer((sessionContainer != null ? sessionContainer
                        : applicationContainer));
                return requestContainer;
            default:
                throw new WaffleException("Invalid context level " + level);
        }
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
        registrarContainer.addComponent(StubServletContext.class);
        registrarContainer.addComponent(StubServletRequest.class);
        try {
            MutablePicoContainer initContainer = new DefaultPicoContainer();
            initContainer.addComponent(registrarContainer);
            initContainer.addComponent(NullComponentMonitor.class);
            initContainer.addComponent(PicoLifecycleStrategy.class);
            initContainer.addComponent(SilentMonitor.class);
            initContainer.addComponent(DefaultMessageResources.class);
            initContainer.addComponent(DefaultParameterResolver.class);
            initContainer.addComponent(DefaultStringTransmuter.class);
            initContainer.addComponent(OgnlValueConverterFinder.class);
            initContainer.addComponent(PicoRegistrar.class);
            initContainer.addComponent(registrarName, Class.forName(registrarName));
            return (Registrar) initContainer.getComponent(registrarName);
        } catch (Exception e) {
            throw new WaffleException("Failed to create Registrar for " + registrarName, e);
        }
    }

    /**
     * ServletContext stub that returns a PicoComponentRegistry backed by the context itself.
     * 
     * @author Mauro Talevi
     */
    public static class StubServletContext implements ServletContext {

        private ComponentRegistry registry;

        public StubServletContext() {
            this.registry = new ComponentRegistry(this);
        }

        public Object getAttribute(String name) {
            if (name.equals(ComponentRegistry.class.getName())) {
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

    /**
     * HttpServletRequest stub that allows request-scoped components to depend on it
     * 
     * @author Mauro Talevi
     */
    public static class StubServletRequest implements HttpServletRequest {

        public String getAuthType() {
            return null;
        }

        public String getContextPath() {
            return null;
        }

        public Cookie[] getCookies() {
            return null;
        }

        public long getDateHeader(String name) {
            return 0;
        }

        public String getHeader(String name) {
            return null;
        }

        public Enumeration<String> getHeaderNames() {
            return null;
        }

        public Enumeration<String> getHeaders(String name) {
            return null;
        }

        public int getIntHeader(String name) {
            return 0;
        }

        public String getMethod() {
            return null;
        }

        public String getPathInfo() {
            return null;
        }

        public String getPathTranslated() {
            return null;
        }

        public String getQueryString() {
            return null;
        }

        public String getRemoteUser() {
            return null;
        }

        public String getRequestURI() {
            return null;
        }

        public StringBuffer getRequestURL() {
            return null;
        }

        public String getRequestedSessionId() {
            return null;
        }

        public String getServletPath() {
            return null;
        }

        public HttpSession getSession() {
            return null;
        }

        public HttpSession getSession(boolean create) {
            return null;
        }

        public Principal getUserPrincipal() {
            return null;
        }

        public boolean isRequestedSessionIdFromCookie() {
            return false;
        }

        public boolean isRequestedSessionIdFromURL() {
            return false;
        }

        public boolean isRequestedSessionIdFromUrl() {
            return false;
        }

        public boolean isRequestedSessionIdValid() {
            return false;
        }

        public boolean isUserInRole(String role) {
            return false;
        }

        public Object getAttribute(String name) {
            return null;
        }

        public Enumeration<String> getAttributeNames() {
            return null;
        }

        public String getCharacterEncoding() {
            return null;
        }

        public int getContentLength() {
            return 0;
        }

        public String getContentType() {
            return null;
        }

        public ServletInputStream getInputStream() throws IOException {
            return null;
        }

        public String getLocalAddr() {
            return null;
        }

        public String getLocalName() {
            return null;
        }

        public int getLocalPort() {
            return 0;
        }

        public Locale getLocale() {
            return null;
        }

        public Enumeration<Locale> getLocales() {
            return null;
        }

        public String getParameter(String name) {
            return null;
        }

        public Map<String, String> getParameterMap() {
            return null;
        }

        public Enumeration<String> getParameterNames() {
            return null;
        }

        public String[] getParameterValues(String name) {
            return null;
        }

        public String getProtocol() {
            return null;
        }

        public BufferedReader getReader() throws IOException {
            return null;
        }

        public String getRealPath(String path) {
            return null;
        }

        public String getRemoteAddr() {
            return null;
        }

        public String getRemoteHost() {
            return null;
        }

        public int getRemotePort() {
            return 0;
        }

        public RequestDispatcher getRequestDispatcher(String path) {
            return null;
        }

        public String getScheme() {
            return null;
        }

        public String getServerName() {
            return null;
        }

        public int getServerPort() {
            return 0;
        }

        public boolean isSecure() {
            return false;
        }

        public void removeAttribute(String name) {
        }

        public void setAttribute(String name, Object o) {
        }

        public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        }
    }

}
