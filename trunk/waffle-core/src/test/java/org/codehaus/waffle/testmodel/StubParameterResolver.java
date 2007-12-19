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
package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.registrar.pico.ParameterResolver;
import org.picocontainer.Parameter;

public class StubParameterResolver implements ParameterResolver {
    public Parameter resolve(Object arg) {
        return null;
    }
}
