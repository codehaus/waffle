/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.context.ContextLevel;
import org.codehaus.waffle.i18n.DefaultMessagesContext;
import org.codehaus.waffle.i18n.MessagesContext;
import org.codehaus.waffle.validation.DefaultErrorsContext;
import org.codehaus.waffle.validation.ErrorsContext;

import java.lang.reflect.Constructor;

/**
 * Manages the Registrar defined in the applications web.xml and executes the method(s) annotated
 * according to the ContextLevel being handled.
 *
 * @author Michael Ward
 */
public class RegistrarAssistant {
    private final Class<?> registrarClass;
    private final Constructor<?> constructor;

    public RegistrarAssistant(Class<?> registrarClass) {
        try {
            this.registrarClass = registrarClass;
            this.constructor = registrarClass.getConstructor(Registrar.class);
        } catch (NoSuchMethodException e) {
            throw new WaffleException(e);
        }
    }

    public void executeDelegatingRegistrar(Registrar delegateRegistrar, ContextLevel contextLevel) throws WaffleException {
        try {
            Registrar registrar = (Registrar) constructor.newInstance(delegateRegistrar);

            if (ContextLevel.APPLICATION.equals(contextLevel)) {
                registrar.application();
            } else if (ContextLevel.SESSION.equals(contextLevel)) {
                registrar.session();
            } else if (ContextLevel.REQUEST.equals(contextLevel)) {
                registrar.request();
                if (!registrar.isRegistered(ErrorsContext.class)) {
                    registrar.register((Object) ErrorsContext.class, DefaultErrorsContext.class);
                }
                if (!registrar.isRegistered(MessagesContext.class)) {
                    registrar.register((Object) MessagesContext.class, DefaultMessagesContext.class);
                }
            }
        } catch (Exception e) {
            throw new WaffleException("Unable to use registrar [" + registrarClass + "]", e);
        }
    }

}
