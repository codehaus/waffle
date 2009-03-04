package org.codehaus.waffle;

public interface ComponentFinder {
    Object getComponent(Class type, String name);
}
