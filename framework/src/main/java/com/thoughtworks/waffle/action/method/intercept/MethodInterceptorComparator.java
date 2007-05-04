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
package com.thoughtworks.waffle.action.method.intercept;

import java.util.Comparator;

public class MethodInterceptorComparator implements Comparator<MethodInterceptor> {

    public int compare(MethodInterceptor first, MethodInterceptor second) {
        if (first instanceof Sortable && second instanceof Sortable) {
            Sortable one = (Sortable) first;
            Sortable two = (Sortable) second;
            return new Integer(one.getIndex()).compareTo(two.getIndex());
        } else if (first instanceof Sortable) {
            return -1; // force to be less
        } else {
            return 1; // force other to be greater
        }
    }

}
