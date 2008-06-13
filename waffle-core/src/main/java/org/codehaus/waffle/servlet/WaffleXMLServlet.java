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
package org.codehaus.waffle.servlet;

import org.codehaus.waffle.view.View;
import org.codehaus.waffle.view.XMLView;

/**
 * Waffle's FrontController for XML serialization.
 *
 * @author Paulo Silveira
 */
@SuppressWarnings("serial")
public class WaffleXMLServlet extends WaffleServlet {

    protected View buildViewToReferrer() {
        return new XMLView();
    }
    
}
