/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar;

import static org.codehaus.waffle.context.ContextLevel.APPLICATION;
import static org.codehaus.waffle.context.ContextLevel.REQUEST;
import static org.codehaus.waffle.context.ContextLevel.SESSION;

import java.lang.reflect.Constructor;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.context.ContextLevel;
import org.codehaus.waffle.i18n.DefaultMessagesContext;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.i18n.MessagesContext;
import org.codehaus.waffle.validation.DefaultErrorsContext;
import org.codehaus.waffle.validation.ErrorsContext;

/**
 * Instantiates the Registrar defined in the application web.xml and executes the method(s) annotated according to the
 * ContextLevel being handled.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class RegistrarAssistant {
    private final Class<?> registrarClass;
    private final Constructor<?> constructor;
    private final MessageResources messageResources;

    public RegistrarAssistant(Class<?> registrarClass, MessageResources messageResources) {
        this.registrarClass = registrarClass;
        this.messageResources = messageResources;
        try {
            this.constructor = registrarClass.getConstructor(Registrar.class);
        } catch (NoSuchMethodException e) {
            String message = messageResources.getMessageWithDefault("registrarConstructorNotFound",
                    "Constructor with single Registrar parameter not found for registrar ''{0}''", registrarClass
                            .getName());
            throw new InvalidRegistrarException(message, e);
        }
    }

    public void executeDelegatingRegistrar(Registrar delegateRegistrar, ContextLevel contextLevel)
            throws WaffleException {
        try {
            Registrar registrar = (Registrar) constructor.newInstance(delegateRegistrar);

            if (APPLICATION.equals(contextLevel)) {
                registrar.application();
            } else if (SESSION.equals(contextLevel)) {
                registrar.session();
            } else if (REQUEST.equals(contextLevel)) {
                registrar.request();
                if (!registrar.isRegistered(ErrorsContext.class)) {
                    registrar.register((Object) ErrorsContext.class, DefaultErrorsContext.class);
                }
                if (!registrar.isRegistered(MessagesContext.class)) {
                    registrar.register((Object) MessagesContext.class, DefaultMessagesContext.class);
                }
            }
        } catch (Exception e) {
            String message = messageResources.getMessageWithDefault("registrarDelegationFailed",
                    "Failed to delegate to registrar ''{0}''", registrarClass.getName());
            throw new InvalidRegistrarException(message, e);
        }
    }
}
