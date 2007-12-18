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
package org.codehaus.waffle.registrar.pico;

import org.codehaus.waffle.registrar.Argument;
import org.codehaus.waffle.registrar.ComponentArgument;
import org.codehaus.waffle.registrar.HttpSessionAttributeArgument;
import org.codehaus.waffle.registrar.RequestAttributeArgument;
import org.codehaus.waffle.registrar.ServletContextAttributeArgument;
import org.codehaus.waffle.registrar.RequestParameterArgument;
import org.codehaus.waffle.WaffleException;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.ComponentParameter;
import org.picocontainer.defaults.ConstantParameter;

public class DefaultPicoContainerParameterResolver implements PicoContainerParameterResolver {

    public Parameter resolve(Object arg) {
        if (arg instanceof Argument) {
            Argument argument = (Argument) arg;

            if (argument instanceof ComponentArgument) {
                return new ComponentParameter(argument.getKey());
            } else if(argument instanceof RequestParameterArgument) {
                return new RequestParameterParameter(argument.getKey().toString());
            } else if(argument instanceof RequestAttributeArgument) {
                return new RequestAttributeParameter(argument.getKey().toString());
            } else if(argument instanceof HttpSessionAttributeArgument) {
                return new HttpSessionAttributeParameter(argument.getKey().toString());
            } else if(argument instanceof ServletContextAttributeArgument) {
                return new ServletContextAttributeParameter(argument.getKey().toString());
            }
        } else {
            return new ConstantParameter(arg);
        }

        throw new WaffleException("Unable to resolve a pico parameter for " + arg);
    }
}
