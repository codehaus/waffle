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
package com.thoughtworks.waffle.servlet;

import com.thoughtworks.waffle.WaffleComponentRegistry;
import com.thoughtworks.waffle.WaffleException;
import com.thoughtworks.waffle.action.ControllerDefinitionFactory;
import com.thoughtworks.waffle.action.DefaultControllerDefinitionFactory;
import com.thoughtworks.waffle.action.ControllerNameResolver;
import com.thoughtworks.waffle.action.DefaultControllerNameResolver;
import com.thoughtworks.waffle.action.method.ActionMethodExecutor;
import com.thoughtworks.waffle.action.method.ActionMethodResponseHandler;
import com.thoughtworks.waffle.action.method.ArgumentResolver;
import com.thoughtworks.waffle.action.method.DefaultActionMethodExecutor;
import com.thoughtworks.waffle.action.method.DefaultActionMethodResponseHandler;
import com.thoughtworks.waffle.action.method.HierarchicalArgumentResolver;
import com.thoughtworks.waffle.action.method.MethodDefinitionFinder;
import com.thoughtworks.waffle.action.method.AnnotatedMethodDefinitionFinder;
import com.thoughtworks.waffle.bind.BindErrorMessageResolver;
import com.thoughtworks.waffle.bind.DataBinder;
import com.thoughtworks.waffle.bind.DefaultBindErrorMessageResolver;
import com.thoughtworks.waffle.bind.OgnlDataBinder;
import com.thoughtworks.waffle.bind.OgnlTypeConverter;
import com.thoughtworks.waffle.context.ContextContainerFactory;
import com.thoughtworks.waffle.context.PicoContextContainerFactory;
import com.thoughtworks.waffle.i18n.DefaultMessageResources;
import com.thoughtworks.waffle.i18n.MessageResources;
import com.thoughtworks.waffle.validation.DefaultValidator;
import com.thoughtworks.waffle.validation.Validator;
import com.thoughtworks.waffle.view.DefaultDispatchAssistant;
import com.thoughtworks.waffle.view.DefaultViewDispatcher;
import com.thoughtworks.waffle.view.DefaultViewResolver;
import com.thoughtworks.waffle.view.DispatchAssistant;
import com.thoughtworks.waffle.view.ViewDispatcher;
import com.thoughtworks.waffle.view.ViewResolver;
import ognl.TypeConverter;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;
import org.picocontainer.defaults.DefaultPicoContainer;

import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * This allows Waffle to have a pluggable architecture.
 *
 * @author Michael Ward
 */
public class PicoWaffleComponentRegistry implements WaffleComponentRegistry {
    private final MutablePicoContainer picoContainer = new DefaultPicoContainer();
    private static final String REGISTER_KEY = "register:";
    private static final String REGISTER_NON_CACHING_KEY = "registerNonCaching:";

    /**
     * Register all waffle required components with the underlying container.
     */
    public PicoWaffleComponentRegistry(ServletContext servletContext) {
        picoContainer.registerComponentInstance(servletContext);

        // register all known components
        register(ActionMethodExecutor.class, DefaultActionMethodExecutor.class, servletContext);
        register(ActionMethodResponseHandler.class, DefaultActionMethodResponseHandler.class, servletContext);
        register(ArgumentResolver.class, HierarchicalArgumentResolver.class, servletContext);
        register(BindErrorMessageResolver.class, DefaultBindErrorMessageResolver.class, servletContext);
        register(ContextContainerFactory.class, PicoContextContainerFactory.class, servletContext);
        register(ControllerDefinitionFactory.class, DefaultControllerDefinitionFactory.class, servletContext);
        register(ControllerNameResolver.class, DefaultControllerNameResolver.class, servletContext);
        register(DataBinder.class, OgnlDataBinder.class, servletContext);
        register(DispatchAssistant.class, DefaultDispatchAssistant.class, servletContext);
        register(MethodDefinitionFinder.class, AnnotatedMethodDefinitionFinder.class, servletContext);
        register(MessageResources.class, DefaultMessageResources.class, servletContext);
        register(TypeConverter.class, OgnlTypeConverter.class, servletContext);
        register(Validator.class, DefaultValidator.class, servletContext);
        register(ViewDispatcher.class, DefaultViewDispatcher.class, servletContext);
        register(ViewResolver.class, DefaultViewResolver.class, servletContext);

        // register other components
        registerOtherComponents(servletContext);
    }

    private void registerOtherComponents(ServletContext servletContext) {
        //noinspection unchecked
        Enumeration<String> enums = servletContext.getInitParameterNames();

        try {
            while (enums != null && enums.hasMoreElements()) {
                String name = enums.nextElement();

                if (name.startsWith(REGISTER_KEY)) {
                    String key = name.split(REGISTER_KEY)[1];
                    Class concreteClass = Class.forName(servletContext.getInitParameter(name));

                    picoContainer.registerComponentImplementation(key, concreteClass);
                } else if (name.startsWith(REGISTER_NON_CACHING_KEY)) {
                    String key = name.split(REGISTER_NON_CACHING_KEY)[1];
                    Class concreteClass = Class.forName(servletContext.getInitParameter(name));

                    ComponentAdapter componentAdapter =
                            new ConstructorInjectionComponentAdapter(key, concreteClass);
                    picoContainer.registerComponent(componentAdapter);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new WaffleException(e);
        }
    }

    /**
     * This method will locate the component Class to use.  Each of the components can be \
     * overwritten by setting <code>context-param</code> in the applications <code>web.xml</code>.
     * <p/>
     * <code>
     * &lt;context-param&gt;
     * &lt;param-name&gt;com.thoughtworks.waffle.actions.ControllerDefinitionFactory&lt;/param-name&gt;
     * &lt;param-value&gt;org.myurl.FooBarControllerFactory&lt;/param-value&gt;
     * &lt;/context-param&gt;
     * </code>
     *
     * @param key            represents the interface Class which the implementation should be registered under.
     * @param defaultClass   represents the Class to use by default (when not over-written).
     * @param servletContext required to obtain the InitParameter defined for the web application.
     * @throws WaffleException
     */
    protected static Class locateComponentClass(Class key, Class defaultClass, ServletContext servletContext) throws WaffleException {
        String className = servletContext.getInitParameter(key.getName());

        if (className == null || className.equals("")) {
            return defaultClass;
        } else {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new WaffleException(e.getMessage(), e);
            }
        }
    }

    /**
     * Register the correct class to the underlying container
     */
    private void register(Class key, Class defaultClass, ServletContext servletContext) throws WaffleException {
        Class actualClass = locateComponentClass(key, defaultClass, servletContext);
        picoContainer.registerComponentImplementation(key, actualClass);
    }

    /**
     * Convenience method for locating and automatically casting a Component from the container.
     */
    public <T> T locateByKey(Object key) {
        //noinspection unchecked
        return (T) picoContainer.getComponentInstance(key);
    }

    public <T> T locateByType(Class<T> t) {
        //noinspection unchecked
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

    public BindErrorMessageResolver getBindErrorMessageResolver() {
        return locateByType(BindErrorMessageResolver.class);
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

    public DataBinder getDataBinder() {
        return locateByType(DataBinder.class);
    }

    public DispatchAssistant getDispatchAssistant() {
        return locateByType(DispatchAssistant.class);
    }

    public MessageResources getMessageResources() {
        return locateByType(MessageResources.class);
    }

    public MethodDefinitionFinder getMethodDefinitionFinder() {
        return locateByType(MethodDefinitionFinder.class);
    }

    public TypeConverter getTypeConverter() {
        return locateByType(TypeConverter.class);
    }

    public Validator getValidator() {
        return locateByType(Validator.class);
    }

    public ViewDispatcher getViewDispatcher() {
        return locateByType(ViewDispatcher.class);
    }

    public ViewResolver getViewResolver() {
        return locateByType(ViewResolver.class);
    }

}
