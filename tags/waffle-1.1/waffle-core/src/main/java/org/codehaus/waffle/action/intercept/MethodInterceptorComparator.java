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
package org.codehaus.waffle.action.intercept;

import java.util.Comparator;
import java.io.Serializable;

/**
 * <p>Comparator implementation that is used by Waffle to ensure that MethodInterceptors are executed in
 * the correct order.</p>
 */
@SuppressWarnings("serial")
public class MethodInterceptorComparator implements Comparator<MethodInterceptor>, Serializable {

    /**
     * Will compare both MethodInterceptors being compared to determine the correct evaluation order.
     *
     * @see org.codehaus.waffle.action.intercept.Sortable
     *
     * @param first the first MethodInterceptor to be compared
     * @param second the second MethodInterceptor to be compared
     * @return a negative integer, zero, or a positive integer as the
     *         first MethodInterceptor is less than, equal to, or greater than the
     *         second. 
     */
    public int compare(MethodInterceptor first, MethodInterceptor second) {
        if (first instanceof Sortable && second instanceof Sortable) {
            Sortable one = (Sortable) first;
            Sortable two = (Sortable) second;
            return one.getIndex().compareTo(two.getIndex());
        } else if (first instanceof Sortable) {
            return -1; // force to be less
        } else {
            return 1; // force other to be greater
        }
    }

}
