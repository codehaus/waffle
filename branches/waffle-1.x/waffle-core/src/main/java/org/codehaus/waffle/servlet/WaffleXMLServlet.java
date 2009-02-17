/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
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
