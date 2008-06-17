/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind.ognl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.action.ArgumentResolver;
import org.codehaus.waffle.bind.BindErrorMessageResolver;
import org.codehaus.waffle.bind.ValueConverterFinder;
import org.codehaus.waffle.controller.ScriptedController;
import org.codehaus.waffle.monitor.BindMonitor;
import org.codehaus.waffle.validation.ErrorsContext;

public abstract class ScriptedControllerDataBinder extends OgnlControllerDataBinder {
    protected final ArgumentResolver argumentResolver;

    public ScriptedControllerDataBinder(ValueConverterFinder valueConverterFinder,
                          BindErrorMessageResolver bindErrorMessageResolver,
                          ArgumentResolver argumentResolver,
                          BindMonitor bindMonitor) {
        super(valueConverterFinder, bindErrorMessageResolver, bindMonitor);
        this.argumentResolver = argumentResolver;
    }

    public void bind(HttpServletRequest request, HttpServletResponse response, ErrorsContext errorsContext, Object controller) {
        if (controller instanceof ScriptedController) {
            bindScriptedController(request, response, errorsContext, (ScriptedController) controller);
        } else {
            // default to standard binding
            super.bind(request, response, errorsContext, controller);
        }
    }

    protected abstract void bindScriptedController(HttpServletRequest request, HttpServletResponse response,
            ErrorsContext errorsContext, ScriptedController controller);

}
