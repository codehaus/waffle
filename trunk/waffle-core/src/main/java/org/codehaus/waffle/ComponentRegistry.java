/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle;


import org.codehaus.waffle.action.ActionMethodExecutor;
import org.codehaus.waffle.action.ActionMethodResponseHandler;
import org.codehaus.waffle.action.ArgumentResolver;
import org.codehaus.waffle.action.MethodDefinitionFinder;
import org.codehaus.waffle.action.MethodNameResolver;
import org.codehaus.waffle.bind.BindErrorMessageResolver;
import org.codehaus.waffle.bind.ControllerDataBinder;
import org.codehaus.waffle.bind.ViewDataBinder;
import org.codehaus.waffle.bind.StringTransmuter;
import org.codehaus.waffle.bind.ValueConverterFinder;
import org.codehaus.waffle.context.ContextContainerFactory;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;
import org.codehaus.waffle.controller.ControllerNameResolver;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.monitor.ActionMonitor;
import org.codehaus.waffle.monitor.BindMonitor;
import org.codehaus.waffle.monitor.ContextMonitor;
import org.codehaus.waffle.monitor.ControllerMonitor;
import org.codehaus.waffle.monitor.RegistrarMonitor;
import org.codehaus.waffle.monitor.ServletMonitor;
import org.codehaus.waffle.monitor.ValidationMonitor;
import org.codehaus.waffle.monitor.ViewMonitor;
import org.codehaus.waffle.validation.Validator;
import org.codehaus.waffle.view.ViewDispatcher;
import org.codehaus.waffle.view.ViewResolver;

/**
 * <p>
 * ComponentRegistry is responsible for maintaining Waffle components and allowing Waffle to have a pluggable
 * architecture.
 * </p>
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public interface ComponentRegistry {

    /**
     * Retrieve a component via the key it was registered under
     *
     * @param key the key the component was registered under
     * @return the component registered
     */
    <T> T locateByKey(Object key);

    /**
     * Retrieve a component by type.  Be cautious of making ambiguous requests.
     *
     * @param t the type of the component requested
     * @return the component registered
     */
    <T> T locateByType(Class<T> t);

    ActionMethodExecutor getActionMethodExecutor();

    ActionMethodResponseHandler getActionMethodResponseHandler();

    ActionMonitor getActionMonitor();

    ArgumentResolver getArgumentResolver();

    BindErrorMessageResolver getBindErrorMessageResolver();

    BindMonitor getBindMonitor();

    ContextContainerFactory getContextContainerFactory();

    ContextMonitor getContextMonitor();

    ControllerDataBinder getControllerDataBinder();

    ControllerDefinitionFactory getControllerDefinitionFactory();

    ControllerMonitor getControllerMonitor();

    ControllerNameResolver getControllerNameResolver();

    MessageResources getMessageResources();

    MethodDefinitionFinder getMethodDefinitionFinder();

    MethodNameResolver getMethodNameResolver();

    RegistrarMonitor getRegistrarMonitor();

    ServletMonitor getServletMonitor();

    StringTransmuter getStringTransmuter();

    ValueConverterFinder getValueConverterFinder();

    Validator getValidator();

    ValidationMonitor getValidationMonitor();

    ViewDataBinder getViewDataBinder();

    ViewDispatcher getViewDispatcher();

    ViewMonitor getViewMonitor();

    ViewResolver getViewResolver();

}
