/*****************************************************************************
 * Copyright (C) 2005 - 2007 Michael Ward                                    *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.action;

import org.codehaus.waffle.WaffleException;

/**
 * This is a specialized exception that will be thrown directly from an ActionMethod.  Exceptions of this type
 * will set the response status code and response body specially.
 */
public class ActionMethodException extends WaffleException {
}
