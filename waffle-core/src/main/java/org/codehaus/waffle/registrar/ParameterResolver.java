/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar;

import org.picocontainer.Parameter;
import org.picocontainer.parameters.ComponentParameter;
import org.picocontainer.parameters.ConstantParameter;
import org.codehaus.waffle.bind.StringTransmuter;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.registrar.Reference;
import org.codehaus.waffle.registrar.ComponentReference;
import org.codehaus.waffle.registrar.RequestParameterReference;
import org.codehaus.waffle.registrar.RequestAttributeReference;
import org.codehaus.waffle.registrar.SessionAttributeReference;
import org.codehaus.waffle.registrar.ServletContextAttributeReference;
import org.codehaus.waffle.WaffleException;

/**
 * Implementations of this interface will find the correct PicoContainer Parameter needed based on the
 * argument being resolved
 *
 * @author Michael Ward
 */
public class ParameterResolver {
    private final StringTransmuter stringTransmuter;
    private final MessageResources messageResources;

    public ParameterResolver(StringTransmuter stringTransmuter) {
        this(stringTransmuter, new DefaultMessageResources());
    }

    public ParameterResolver(StringTransmuter stringTransmuter, MessageResources messageResources) {
        this.stringTransmuter = stringTransmuter;
        this.messageResources = messageResources;
    }

    /**
     * An argument of type Reference will be mapped to the correct Parameter implemntation while other types
     * will be treated as a constant ({@code ConstantParameter})
     *
     * @param argument the argument to be resolved
     * @return the correct Parameter.
     */
    public Parameter resolve(Object argument) {
        if (argument instanceof Reference) {
            Reference reference = (Reference) argument;

            if (reference instanceof ComponentReference) {
                return new ComponentParameter(reference.getKey());
            } else if(reference instanceof RequestParameterReference) {
                RequestParameterReference requestParameterReference = (RequestParameterReference) reference;
                return new RequestParameterParameter(reference.getKey().toString(), stringTransmuter, requestParameterReference.getDefaultValue());
            } else if(reference instanceof RequestAttributeReference) {
                return new RequestAttributeParameter(reference.getKey().toString());
            } else if(reference instanceof SessionAttributeReference) {
                return new SessionAttributeParameter(reference.getKey().toString());
            } else if(reference instanceof ServletContextAttributeReference) {
                return new ServletContextAttributeParameter(reference.getKey().toString());
            }
        } else {
            return new ConstantParameter(argument);
        }

        String message = messageResources.getMessageWithDefault("parameterNotResolved", "Unable to resolve parameter for argument ''{0}''", argument);
        throw new WaffleException(message);
    }
}
