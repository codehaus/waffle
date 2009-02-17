/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context.pico;

import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.monitor.ContextMonitor;
import org.codehaus.waffle.monitor.RegistrarMonitor;
import org.codehaus.waffle.registrar.pico.ParameterResolver;

public abstract class ScriptedPicoContextContainerFactory extends PicoContextContainerFactory {

    public ScriptedPicoContextContainerFactory(MessageResources messageResources,
                                                ContextMonitor contextMonitor,
                                                RegistrarMonitor registrarMonitor,
                                                ParameterResolver parameterResolver) {
        super(messageResources, contextMonitor, registrarMonitor, parameterResolver);
    }

    @Override
    public ContextContainer buildApplicationContextContainer() {
        ContextContainer contextContainer = super.buildApplicationContextContainer();

        registerScriptComponents(contextContainer);

        return contextContainer;
    }

    protected abstract void registerScriptComponents(ContextContainer contextContainer);

}
