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
package org.codehaus.waffle.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author Michael Ward
 */
public class DefaultDispatchAssistant implements DispatchAssistant {
    public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";

    public void redirect(HttpServletRequest request,
                         HttpServletResponse response,
                         Map model,
                         String path) throws IOException {
        String encoding = request.getCharacterEncoding();

        if (encoding == null) {
            encoding = DEFAULT_CHARACTER_ENCODING;
        }

        StringBuilder url = new StringBuilder(path);
        Object[] keys = model.keySet().toArray();

        // add parameters to the url using each key value pair from the model.
        for (int i = 0; i < keys.length; i++) {
            Object key = keys[i];

            if (i == 0 && url.indexOf("?") == -1) {
                url.append("?");
            } else {
                url.append("&");
            }

            // append key and value
            url.append(URLEncoder.encode(key.toString(), encoding))
                    .append("=")
                    .append(URLEncoder.encode(model.get(key).toString(), encoding));
        }

        response.setCharacterEncoding(encoding);
        response.sendRedirect(response.encodeRedirectURL(url.toString()));
    }

    public void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws IOException, ServletException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
        requestDispatcher.forward(request, response);
    }
}
