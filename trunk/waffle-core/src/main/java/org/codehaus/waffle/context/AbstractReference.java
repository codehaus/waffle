/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context;

/**
 * Simple abstract implementation which manages the associated <i>key</i>.
 *
 * @author Michael Ward
 */
public abstract class AbstractReference implements Reference {
    private Object key;

    public AbstractReference(Object key) {
        this.key = key;
    }

    public Object getKey() {
        return key;
    }
}
