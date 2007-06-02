package org.codehaus.waffle.bind;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementors of this class allow for properties from a controller to be exposed as request attributes.  This
 * simplifies view development.
 */
public interface RequestAttributeBinder {

    void bind(HttpServletRequest request, Object controller);
}
