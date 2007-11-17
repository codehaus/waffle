/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.context.pico;

import java.util.Enumeration;

import javax.servlet.ServletContext;

import ognl.TypeConverter;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.action.ActionMethodExecutor;
import org.codehaus.waffle.action.ActionMethodResponseHandler;
import org.codehaus.waffle.action.AnnotatedMethodDefinitionFinder;
import org.codehaus.waffle.action.ArgumentResolver;
import org.codehaus.waffle.action.DefaultActionMethodResponseHandler;
import org.codehaus.waffle.action.HierarchicalArgumentResolver;
import org.codehaus.waffle.action.InterceptingActionMethodExecutor;
import org.codehaus.waffle.action.MethodDefinitionFinder;
import org.codehaus.waffle.action.MethodNameResolver;
import org.codehaus.waffle.action.RequestParameterMethodNameResolver;
import org.codehaus.waffle.bind.BindErrorMessageResolver;
import org.codehaus.waffle.bind.DataBinder;
import org.codehaus.waffle.bind.DefaultBindErrorMessageResolver;
import org.codehaus.waffle.bind.IntrospectingRequestAttributeBinder;
import org.codehaus.waffle.bind.OgnlDataBinder;
import org.codehaus.waffle.bind.DelegatingTypeConverter;
import org.codehaus.waffle.bind.RequestAttributeBinder;
import org.codehaus.waffle.context.ContextContainerFactory;
import org.codehaus.waffle.controller.ContextControllerDefinitionFactory;
import org.codehaus.waffle.controller.ContextPathControllerNameResolver;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;
import org.codehaus.waffle.controller.ControllerNameResolver;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.monitor.ActionMonitor;
import org.codehaus.waffle.monitor.BindMonitor;
import org.codehaus.waffle.monitor.ContextMonitor;
import org.codehaus.waffle.monitor.ControllerMonitor;
import org.codehaus.waffle.monitor.RegistrarMonitor;
import org.codehaus.waffle.monitor.ServletMonitor;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.monitor.ValidationMonitor;
import org.codehaus.waffle.monitor.ViewMonitor;
import org.codehaus.waffle.validation.DefaultValidator;
import org.codehaus.waffle.validation.Validator;
import org.codehaus.waffle.view.DefaultViewDispatcher;
import org.codehaus.waffle.view.DefaultViewResolver;
import org.codehaus.waffle.view.ViewDispatcher;
import org.codehaus.waffle.view.ViewResolver;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;
import org.picocontainer.defaults.DefaultPicoContainer;

/**
 * PicoContainer-based implementation of Waffle's ComponentRegistry
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class PicoComponentRegistry implements ComponentRegistry {
    private static final String REGISTER_KEY = "register:";
    private static final String REGISTER_NON_CACHING_KEY = "registerNonCaching:";
    private final MutablePicoContainer picoContainer = new DefaultPicoContainer();

    /**
     * Register all waffle required components with the underlying container.
     */
    public PicoComponentRegistry(ServletContext servletContext) {
        picoContainer.registerComponentInstance(servletContext);
        
        // register all known components
        register(ActionMethodExecutor.class, InterceptingActionMethodExecutor.class, servletContext);
        register(ActionMethodResponseHandler.class, DefaultActionMethodResponseHandler.class, servletContext);
        register(ArgumentResolver.class, HierarchicalArgumentResolver.class, servletContext);
        register(MethodDefinitionFinder.class, AnnotatedMethodDefinitionFinder.class, servletContext);
        register(MethodNameResolver.class, RequestParameterMethodNameResolver.class, servletContext);
        register(BindErrorMessageResolver.class, DefaultBindErrorMessageResolver.class, servletContext);
        register(DataBinder.class, OgnlDataBinder.class, servletContext);
        register(RequestAttributeBinder.class, IntrospectingRequestAttributeBinder.class, servletContext);
        register(TypeConverter.class, DelegatingTypeConverter.class, servletContext);
        register(ContextContainerFactory.class, PicoContextContainerFactory.class, servletContext);
        register(ControllerDefinitionFactory.class, ContextControllerDefinitionFactory.class, servletContext);
        register(ControllerNameResolver.class, ContextPathControllerNameResolver.class, servletContext);
        register(MessageResources.class, DefaultMessageResources.class, servletContext);
        register(ActionMonitor.class, SilentMonitor.class, servletContext);
        register(BindMonitor.class, SilentMonitor.class, servletContext);
        register(ContextMonitor.class, SilentMonitor.class, servletContext);
        register(ControllerMonitor.class, SilentMonitor.class, servletContext);
        register(RegistrarMonitor.class, SilentMonitor.class, servletContext);
        register(ServletMonitor.class, SilentMonitor.class, servletContext);
        register(ValidationMonitor.class, SilentMonitor.class, servletContext);
        register(ViewMonitor.class, SilentMonitor.class, servletContext);
        register(Validator.class, DefaultValidator.class, servletContext);
        register(ViewDispatcher.class, DefaultViewDispatcher.class, servletContext);
        register(ViewResolver.class, DefaultViewResolver.class, servletContext);

        // register other components
        registerOtherComponents(servletContext);
    }

    @SuppressWarnings("unchecked")
    private void registerOtherComponents(ServletContext servletContext) {
        //noinspection unchecked
        Enumeration<String> enums = servletContext.getInitParameterNames();

        while (enums != null && enums.hasMoreElements()) {
            String name = enums.nextElement();

            if (name.startsWith(REGISTER_KEY)) {
                String key = name.split(REGISTER_KEY)[1];
                Class concreteClass = loadClass(servletContext.getInitParameter(name));

                picoContainer.registerComponentImplementation(key, concreteClass);
            } else if (name.startsWith(REGISTER_NON_CACHING_KEY)) {
                String key = name.split(REGISTER_NON_CACHING_KEY)[1];
                Class concreteClass = loadClass(servletContext.getInitParameter(name));

                ComponentAdapter componentAdapter =
                        new ConstructorInjectionComponentAdapter(key, concreteClass);
                picoContainer.registerComponent(componentAdapter);
            }
        }
    }

    /**
     * This method will locate the component Class to use.  Each of the components can be 
     * overwritten by setting <code>context-param</code> in the applications <code>web.xml</code>.
     * <p/>
     * <code>
     * &lt;context-param&gt;
     *   &lt;param-name&gt;org.codehaus.waffle.actions.ControllerDefinitionFactory&lt;/param-name&gt;
     *   &lt;param-value&gt;org.myurl.FooBarControllerFactory&lt;/param-value&gt;
     * &lt;/context-param&gt;
     * </code>
     *
     * @param key            represents the component key which the implementation should be registered under.
     * @param defaultClass   represents the Class to use by default (when not over-written).
     * @param servletContext required to obtain the InitParameter defined for the web application.
     * @throws WaffleException
     */
    protected static Class<?> locateComponentClass(Object key, Class<?> defaultClass, ServletContext servletContext) throws WaffleException {
        String parameterName;
        if ( key instanceof Class ){
            parameterName = ((Class<?>)key).getName();
        } else if ( key instanceof String ){
            parameterName = (String) key;
        } else {
            return defaultClass;            
        }
        
        String className = servletContext.getInitParameter(parameterName);

        if (className == null || className.length() == 0) {
            return defaultClass;
        } else {
            return loadClass(className);
        }
    }

    /**
     * Loads class for a given name
     * 
     * @param className the Class name
     * @return The Class for the name
     * @throws WaffleException if class not found
     */
    private static Class<?> loadClass(String className) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new WaffleException(e.getMessage(), e);
        }
    }

    /**
     * Register the component class in the underlying container
     */
    private void register(Object key, Class<?> defaultClass, ServletContext servletContext) throws WaffleException {
        Class<?> componentClass = locateComponentClass(key, defaultClass, servletContext);
        picoContainer.registerComponentImplementation(key, componentClass);
    }

    /**
     * Convenience method for locating and automatically casting a Component from the container.
     */
    @SuppressWarnings("unchecked")
    public <T> T locateByKey(Object key) {
        return (T) picoContainer.getComponentInstance(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T locateByType(Class<T> t) {
        return (T) picoContainer.getComponentInstanceOfType(t);
    }

    public ActionMethodExecutor getActionMethodExecutor() {
        return locateByType(ActionMethodExecutor.class);
    }

    public ActionMethodResponseHandler getActionMethodResponseHandler() {
        return locateByType(ActionMethodResponseHandler.class);
    }

    public ArgumentResolver getArgumentResolver() {
        return locateByType(ArgumentResolver.class);
    }

    public MethodDefinitionFinder getMethodDefinitionFinder() {
        return locateByType(MethodDefinitionFinder.class);
    }

    public MethodNameResolver getMethodNameResolver() {
        return locateByType(MethodNameResolver.class);
    }
    public BindErrorMessageResolver getBindErrorMessageResolver() {
        return locateByType(BindErrorMessageResolver.class);
    }

    public DataBinder getDataBinder() {
        return locateByType(DataBinder.class);
    }

    public RequestAttributeBinder getRequestAttributeBinder() {
        return locateByType(RequestAttributeBinder.class);
    }

    public TypeConverter getTypeConverter() {
        return locateByType(TypeConverter.class);
    }

    public ContextContainerFactory getContextContainerFactory() {
        return locateByType(ContextContainerFactory.class);
    }

    public ControllerNameResolver getControllerNameResolver() {
        return locateByType(ControllerNameResolver.class);
    }

    public ControllerDefinitionFactory getControllerDefinitionFactory() {
        return locateByType(ControllerDefinitionFactory.class);
    }

    public MessageResources getMessageResources() {
        return locateByType(MessageResources.class);
    }

    public ActionMonitor getActionMonitor() {
        return locateByType(ActionMonitor.class);
    }

    public BindMonitor getBindMonitor() {
        return locateByType(BindMonitor.class);
    }

    public ContextMonitor getContextMonitor() {
        return locateByType(ContextMonitor.class);
    }

    public ControllerMonitor getControllerMonitor() {
        return locateByType(ControllerMonitor.class);
    }

    public RegistrarMonitor getRegistrarMonitor() {
        return locateByType(RegistrarMonitor.class);
    }

    public ServletMonitor getServletMonitor() {
        return locateByType(ServletMonitor.class);
    }
    
    public Validator getValidator() {
        return locateByType(Validator.class);
    }

    public ValidationMonitor getValidationMonitor() {
        return locateByType(ValidationMonitor.class);
    }

    public ViewDispatcher getViewDispatcher() {
        return locateByType(ViewDispatcher.class);
    }

    public ViewMonitor getViewMonitor() {
        return locateByType(ViewMonitor.class);
    }

    public ViewResolver getViewResolver() {
        return locateByType(ViewResolver.class);
    }

}
