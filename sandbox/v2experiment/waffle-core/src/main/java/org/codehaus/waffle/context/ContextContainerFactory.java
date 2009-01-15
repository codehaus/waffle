/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.LifecycleStrategy;
import org.picocontainer.PicoContainer;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.monitors.NullComponentMonitor;
import org.codehaus.waffle.context.pico.WaffleLifecycleStrategy;
import org.codehaus.waffle.context.pico.HttpSessionComponentAdapter;
import org.codehaus.waffle.monitor.RegistrarMonitor;
import org.codehaus.waffle.monitor.ContextMonitor;
import org.codehaus.waffle.registrar.pico.ParameterResolver;
import org.codehaus.waffle.registrar.pico.PicoRegistrar;
import org.codehaus.waffle.registrar.RegistrarAssistant;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.Constants;
import org.codehaus.waffle.WaffleException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Implementors of this interface are responsible for maintaining the components defined in an applications Registrar.
 * This abstraction of the container relies on its ability to maintain trees of containers which allows Waffle to work
 * so well.
 * 
 * @author Michael Ward
 */
public class ContextContainerFactory {
    private final ComponentMonitor picoComponentMonitor = new NullComponentMonitor();
    private final LifecycleStrategy picoLifecycleStrategy = new WaffleLifecycleStrategy(picoComponentMonitor);
    private final RegistrarMonitor registrarMonitor;
    private final ParameterResolver parameterResolver;

    protected MessageResources getMessageResources() {
        return messageResources;
    }

    private final MessageResources messageResources;
    private RegistrarAssistant registrarAssistant;
    private MutablePicoContainer applicationContextContainer;
    private final ContextMonitor contextMonitor;

    public ContextContainerFactory(MessageResources messageResources,
                                       ContextMonitor contextMonitor,
                                       RegistrarMonitor registrarMonitor,
                                       ParameterResolver parameterResolver) {
        this.messageResources = messageResources;
        this.contextMonitor = contextMonitor;
        this.registrarMonitor = registrarMonitor;
        this.parameterResolver = parameterResolver;
    }

    public MutablePicoContainer buildApplicationContextContainer() {
        return new ContextContainer(buildMutablePicoContainer(null), messageResources);
    }

    public MutablePicoContainer buildSessionLevelContainer() {
        MutablePicoContainer delegate = buildMutablePicoContainer(applicationContextContainer);
        delegate.addComponent(new HttpSessionComponentAdapter());

        ContextContainer sessionContextContainer = new ContextContainer(delegate, messageResources);
        registrarAssistant.executeDelegatingRegistrar(createRegistrar(sessionContextContainer, picoLifecycleStrategy), ContextLevel.SESSION);
        getContextMonitor().sessionContextContainerCreated(applicationContextContainer);
        return sessionContextContainer;
    }

    public MutablePicoContainer buildRequestLevelContainer(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            ContextContainer sessionContextContainer = (ContextContainer) session.getAttribute(Constants.SESSION_CONTAINER_KEY);
            if (sessionContextContainer == null) {
                sessionContextContainer = (ContextContainer) buildSessionLevelContainer();
                session.setAttribute(Constants.SESSION_CONTAINER_KEY, sessionContextContainer);
                sessionContextContainer.start();
            }
            MutablePicoContainer delegate = sessionContextContainer.getDelegate();

            ContextContainer requestContextContainer = new ContextContainer(buildMutablePicoContainer(delegate), messageResources);
            registrarAssistant.executeDelegatingRegistrar(createRegistrar(requestContextContainer, picoLifecycleStrategy), ContextLevel.REQUEST);
            getContextMonitor().requestContextContainerCreated(sessionContextContainer);
            return requestContextContainer;
        } finally {
//TODO: setting the locale from the request (which will by default return the server default locale)
//      does not seem necessary given a default locale is already configured out-of-the-box
//      but more seriously it overrides any configuration set via the MessageResourcesConfiguration
//      hence impeding any real configurability.  Need a clearer usecase for enabling this (MT)
//            messageResources.useLocale(request.getLocale());
        }
    }

    public Registrar createRegistrar(MutablePicoContainer contextContainer, LifecycleStrategy lifecycleStrategy) {
        Registrar registrar = new PicoRegistrar(contextContainer, parameterResolver, picoLifecycleStrategy,
                registrarMonitor, picoComponentMonitor, messageResources);
        getContextMonitor().registrarCreated(registrar, registrarMonitor);
        return registrar;
    }

    private MutablePicoContainer buildMutablePicoContainer(PicoContainer parent) {
        return new DefaultPicoContainer(picoComponentMonitor, picoLifecycleStrategy, parent);
    }

    protected ComponentMonitor getPicoComponentMonitor() {
        return picoComponentMonitor;
    }

    protected RegistrarMonitor getRegistrarMonitor() {
        return registrarMonitor;
    }

    protected ParameterResolver getParameterResolver() {
        return parameterResolver;
    }

    public RegistrarAssistant getRegistrarAssistant() {
        return registrarAssistant;
    }

    public void initialize(ServletContext servletContext) throws WaffleException {
        try {
            initializeRegistrar(servletContext);
            servletContext.setAttribute(ContextContainerFactory.class.getName(), this); // register self to context
            contextMonitor.contextInitialized();
        } catch (WaffleException e) {
            contextMonitor.contextInitializationFailed(e);
            throw e; // re-throwing exception after failure event has been monitored
        }
    }

    /**
     * Create the Registrar from the ServletContext's InitParameter.
     *
     * @param servletContext
     */
    private void initializeRegistrar(ServletContext servletContext) {
        String registrarClassName = servletContext.getInitParameter(Registrar.class.getName());

        try {
            ClassLoader loader = this.getClass().getClassLoader();
            Class<?> registrarClass = loader.loadClass(registrarClassName);
            registrarAssistant = new RegistrarAssistant(registrarClass, messageResources);
        } catch (ClassNotFoundException e) {
            contextMonitor.registrarNotFound(registrarClassName);
            String message = messageResources.getMessageWithDefault("registrarNotFound",
                    "Registrar ''{0}'' defined as context-param in web.xml could not be found.", registrarClassName);
            throw new WaffleException(message, e);
        }

        // build application context container
        applicationContextContainer = buildApplicationContextContainer();
        applicationContextContainer.addComponent(servletContext);
        applicationContextContainer.addComponent(messageResources);

        buildApplicationLevelRegistry();
        applicationContextContainer.start();
        contextMonitor.applicationContextContainerStarted();
    }

    public void destroy() {
        if (applicationContextContainer != null) {
            applicationContextContainer.stop();
            applicationContextContainer.dispose();
            applicationContextContainer = null;
            contextMonitor.applicationContextContainerDestroyed();
        }
    }

    private void buildApplicationLevelRegistry() {
        Registrar registrar = createRegistrar(applicationContextContainer, picoLifecycleStrategy);
        registrarAssistant.executeDelegatingRegistrar(registrar, ContextLevel.APPLICATION);
    }

    public MutablePicoContainer getApplicationContextContainer() {
        return applicationContextContainer;
    }

    protected ContextMonitor getContextMonitor() {
        return contextMonitor;
    }
}
