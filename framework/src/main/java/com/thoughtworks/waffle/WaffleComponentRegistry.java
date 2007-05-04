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
package com.thoughtworks.waffle;

import com.thoughtworks.waffle.action.ControllerDefinitionFactory;
import com.thoughtworks.waffle.action.ControllerNameResolver;
import com.thoughtworks.waffle.action.method.ActionMethodExecutor;
import com.thoughtworks.waffle.action.method.ActionMethodResponseHandler;
import com.thoughtworks.waffle.action.method.ArgumentResolver;
import com.thoughtworks.waffle.action.method.MethodDefinitionFinder;
import com.thoughtworks.waffle.bind.BindErrorMessageResolver;
import com.thoughtworks.waffle.bind.DataBinder;
import com.thoughtworks.waffle.context.ContextContainerFactory;
import com.thoughtworks.waffle.i18n.MessageResources;
import com.thoughtworks.waffle.validation.Validator;
import com.thoughtworks.waffle.view.DispatchAssistant;
import com.thoughtworks.waffle.view.ViewDispatcher;
import com.thoughtworks.waffle.view.ViewResolver;
import ognl.TypeConverter;

public interface WaffleComponentRegistry {
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

    DispatchAssistant getDispatchAssistant();

    MessageResources getMessageResources();

    MethodDefinitionFinder getMethodDefinitionFinder();

    TypeConverter getTypeConverter();

    Validator getValidator();

    ViewDispatcher getViewDispatcher();

    ViewResolver getViewResolver();
}
