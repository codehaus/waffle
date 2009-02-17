/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context;

public class RequestLevelContainer {
    private final static ThreadLocal<ContextContainer> REQUEST_CONTAINER = new ThreadLocal<ContextContainer>();

    private RequestLevelContainer() {
    }

    public static ContextContainer get() {
        return REQUEST_CONTAINER.get();
    }

    public static void set(ContextContainer container) {
        REQUEST_CONTAINER.set(container);
    }
}
