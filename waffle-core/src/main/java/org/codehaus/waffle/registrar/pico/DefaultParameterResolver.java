/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.registrar.pico;

import org.codehaus.waffle.registrar.Reference;
import org.codehaus.waffle.registrar.ComponentReference;
import org.codehaus.waffle.registrar.SessionAttributeReference;
import org.codehaus.waffle.registrar.RequestAttributeReference;
import org.codehaus.waffle.registrar.ServletContextAttributeReference;
import org.codehaus.waffle.registrar.RequestParameterReference;
import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.bind.StringTransmuter;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.ComponentParameter;
import org.picocontainer.defaults.ConstantParameter;

/**
 * This base implementation will determine the correct {@code Parameter} based on the argument being resolved.
 *
 * @author Michael Ward
 */
public class DefaultParameterResolver implements ParameterResolver {
    private final StringTransmuter stringTransmuter;

    public DefaultParameterResolver(StringTransmuter stringTransmuter) {
        this.stringTransmuter = stringTransmuter;
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

        throw new WaffleException("Unable to resolve a pico parameter for " + argument);
    }
}
