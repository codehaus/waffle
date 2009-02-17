/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.i18n;

import java.util.Locale;

/**
 * Represents message resources for a given URI and locale. The <a
 * href="http://en.wikipedia.org/wiki/Uniform_Resource_Identifier">URI</a> can be any string encoding the name or
 * location of the message resources, and it's the responsibility of the implementing classes to specify the decoding.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public interface MessageResources {

    /**
     * @deprecated Use getURI()
     */
    String getResource();

    /**
     * @deprecated Use useURI(String)
     */
    void useResource(String resource);
    
    String getURI();

    void useURI(String uri);

    Locale getLocale();

    void useLocale(Locale locale);

    String getMessage(String key, Object... arguments);

    String getMessageWithDefault(String key, String defaultValue, Object... arguments);

}
