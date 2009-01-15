/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context;

import org.picocontainer.MutablePicoContainer;

public class RequestLevelContainer {
    private final static ThreadLocal<MutablePicoContainer> REQUEST_CONTAINER = new ThreadLocal<MutablePicoContainer>();

    private RequestLevelContainer() {
    }

    public static MutablePicoContainer get() {
        return REQUEST_CONTAINER.get();
    }

    public static void set(MutablePicoContainer container) {
        REQUEST_CONTAINER.set(container);
    }
}
