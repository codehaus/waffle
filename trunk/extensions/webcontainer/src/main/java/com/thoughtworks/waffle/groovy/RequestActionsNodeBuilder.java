package org.codehaus.waffle.groovy;

import groovy.util.NodeBuilder;
import org.nanocontainer.webcontainer.PicoContextHandler;

import java.util.Map;

public class RequestActionsNodeBuilder extends NodeBuilder {
    private final PicoContextHandler context;

    public RequestActionsNodeBuilder(PicoContextHandler context) {
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
