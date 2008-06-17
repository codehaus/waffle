/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.view;

import static java.text.MessageFormat.format;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.monitor.ViewMonitor;

/**
 * The ViewDispatcher handles redirecting/forwarding/exporting to the view
 *
 * @author Michael Ward
 * @author Paulo Silveira
 * @author Mauro Talevi
 */
public class DefaultViewDispatcher implements ViewDispatcher {
    static final String ATTACHMENT_FILENAME = "attachment; filename={0}";
    static final String CONTENT_DISPOSITION_HEADER = "content-disposition";
    static final String LOCATION_HEADER = "Location";
    private final ViewResolver viewResolver;
    private final ViewMonitor viewMonitor;

    public DefaultViewDispatcher(ViewResolver viewResolver, ViewMonitor viewMonitor) {
        this.viewResolver = viewResolver;
        this.viewMonitor = viewMonitor;
    }

    // todo may need to handle ... http://java.sun.com/products/servlet/Filters.html for Character Encoding from request
    public void dispatch(HttpServletRequest request, HttpServletResponse response, View view) throws IOException,
            ServletException {
        String path = viewResolver.resolve(view);

        if (view instanceof ExportView) {
            ExportView exportView = (ExportView) view;
            response.setContentType(exportView.getContentType());
            response.setHeader(CONTENT_DISPOSITION_HEADER, format(ATTACHMENT_FILENAME, exportView.getFilename()));
            response.getOutputStream().write(exportView.getContent());
        } else if (view instanceof RedirectView) {
            RedirectView redirectView = (RedirectView) view;
            response.setStatus(redirectView.getStatusCode());
            response.setHeader(LOCATION_HEADER, path);
            viewMonitor.viewRedirected(redirectView);
        } else if (view instanceof ResponderView) {
            ResponderView responderView = (ResponderView) view;
            responderView.respond(request, response);
            viewMonitor.viewResponded(responderView);
        } else {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
            requestDispatcher.forward(request, response);
            viewMonitor.viewForwarded(path);
        }
    }
}
