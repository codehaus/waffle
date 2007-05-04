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
package com.thoughtworks.waffle.taglib;

import com.thoughtworks.waffle.validation.ErrorsContext;

import java.util.List;

public class WaffleFunctions {

    private WaffleFunctions() {
        // should not be instantiated
    }

    public static List findFieldErrors(ErrorsContext errorsContext, String fieldName) {
        return errorsContext.getFieldErrorMessages(fieldName);
    }

}
