package org.codehaus.waffle.pico;

import org.codehaus.waffle.ComponentFinder;
import org.picocontainer.MutablePicoContainer;

public class PicoComponentFinder implements ComponentFinder {
    private final MutablePicoContainer contextContainer;

    public PicoComponentFinder(MutablePicoContainer contextContainer) {
        this.contextContainer = contextContainer;
    }

    public Object getComponent(Class type, String name) {
        return contextContainer.getComponent(name);
    }
}
