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
package org.codehaus.waffle;

import ognl.TypeConverter;

import org.codehaus.waffle.action.ActionMethodExecutor;
import org.codehaus.waffle.action.ActionMethodResponseHandler;
import org.codehaus.waffle.action.ArgumentResolver;
import org.codehaus.waffle.action.MethodDefinitionFinder;
import org.codehaus.waffle.action.MethodNameResolver;
import org.codehaus.waffle.bind.BindErrorMessageResolver;
import org.codehaus.waffle.bind.DataBinder;
import org.codehaus.waffle.bind.RequestAttributeBinder;
import org.codehaus.waffle.context.ContextContainerFactory;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;
import org.codehaus.waffle.controller.ControllerNameResolver;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.monitor.ActionMonitor;
import org.codehaus.waffle.validation.Validator;
import org.codehaus.waffle.view.ViewDispatcher;
import org.codehaus.waffle.view.ViewResolver;

/**
 * Component registry which allows Waffle to have a pluggable architecture.
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public interface ComponentRegistry {
    <T> T locateByKey(Object key);

    <T> T locateByType(Class<T> t);

    ActionMethodExecutor getActionMethodExecutor();

    ActionMethodResponseHandler getActionMethodResponseHandler();

    ArgumentResolver getArgumentResolver();

    BindErrorMessageResolver getBindErrorMessageResolver();

    ContextContainerFactory getContextContainerFactory();

    ControllerNameResolver getControllerNameResolver();

    ControllerDefinitionFactory getControllerDefinitionFactory();

    DataBinder getDataBinder();

    MessageResources getMessageResources();

    MethodDefinitionFinder getMethodDefinitionFinder();

    MethodNameResolver getMethodNameResolver();

    ActionMonitor getMonitor();

    RequestAttributeBinder getRequestAttributeBinder();

    TypeConverter getTypeConverter();

    Validator getValidator();

    ViewDispatcher getViewDispatcher();

    ViewResolver getViewResolver();
}
