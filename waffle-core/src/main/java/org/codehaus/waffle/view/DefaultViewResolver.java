/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.view;

import static org.codehaus.waffle.Constants.ERRORS_VIEW_KEY;
import static org.codehaus.waffle.Constants.VIEW_PREFIX_KEY;
import static org.codehaus.waffle.Constants.VIEW_SUFFIX_KEY;

import java.util.Properties;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.controller.ControllerDefinition;

/**
 * The default ViewResolver simply returns the path of the View being resolved.
 * It holds the default values of the view properties configurable in the web.xml:
 * <ul>
 * <il>"view.prefix": "/"</li>
 * <il>"view.suffix": ".jspx"</li>
 * <li>"errors.view": "errors"</li>
 * </ul>
 * and resolves a view path as [view.prefix][view.name][view.suffix], where the 
 * view name can also be configured as a view property for each controller name,
 * defaulting to the controller name itself if none is found.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class DefaultViewResolver implements ViewResolver {

    private static final String DEFAULT_VIEW_PREFIX = "/";
    private static final String DEFAULT_VIEW_SUFFIX = ".jspx";
    private static final String DEFAULT_ERRORS_VIEW = "errors";

    Properties viewProperties = new Properties();

    public String resolve(View view) {
        if (view.getPath() != null) {
            return view.getPath();
        } else if (view.getControllerDefinition() != null) {
            return viewPath(view.getControllerDefinition());
        } else {
            throw new WaffleException("Unable to resolve view " + view);
        }
    }

    private String viewPath(ControllerDefinition controllerDefinition) {
        String viewPrefix = viewProperty(VIEW_PREFIX_KEY, DEFAULT_VIEW_PREFIX);
        String viewSuffix = viewProperty(VIEW_SUFFIX_KEY, DEFAULT_VIEW_SUFFIX);
        String viewName = viewName(controllerDefinition);
        return viewPrefix + viewName + viewSuffix;
    }

    private String viewName(ControllerDefinition controllerDefinition) {
        if ( ERRORS_VIEW_KEY.equals(controllerDefinition.getName())){
            return viewProperty(ERRORS_VIEW_KEY, DEFAULT_ERRORS_VIEW);
        }
        return viewProperty(controllerDefinition.getName(), controllerDefinition.getName());
    }

    public void configureViews(Properties viewProperties) {
        this.viewProperties.putAll(viewProperties);
    }

    public void configureView(String key, String value) {
        this.viewProperties.setProperty(key, value);        
    }

    private String viewProperty(String key, String defaultValue) {
        String value = viewProperties.getProperty(key);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }


}
