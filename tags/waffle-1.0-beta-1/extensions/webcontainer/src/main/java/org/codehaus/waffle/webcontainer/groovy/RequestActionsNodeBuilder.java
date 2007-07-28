package org.codehaus.waffle.webcontainer.groovy;

import groovy.util.NodeBuilder;
import org.nanocontainer.webcontainer.PicoContext;

import java.util.Map;

public class RequestActionsNodeBuilder extends NodeBuilder {
    private final PicoContext context;

    public RequestActionsNodeBuilder(PicoContext context) {
        this.context = context;
    }


    protected Object createNode(Object name, Map attributes) {
        if (name.equals("register")) {
            Object clazz = attributes.remove("class"); // could be string or Class
            // register application
            return null; // err something really
        } else {
            return null;
        }
    }
}
