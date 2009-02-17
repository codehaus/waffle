/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.i18n;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Merges resource bundles into a {@link PropertyResourceBundle}.
 * The merged resource bundle will contain all distinct keys from all bundles,
 * and any overlapping keys from a given bundle will the overwritten by the next.
 * It thus employs a last-key-wins strategy.
 * 
 * @author Mauro Talevi
 */
public class ResourceBundleMerger {

    private static final ResourceBundle EMPTY_BUNDLE = new EmptyResourceBundle();

    private final ByteArrayOutputStream stream;

    public ResourceBundleMerger() {
        this(new ByteArrayOutputStream());
    }

    public ResourceBundleMerger(ByteArrayOutputStream stream) {
        this.stream = stream;
    }

    public ResourceBundle merge(List<ResourceBundle> bundles) {
        Properties merged = new Properties();
        for (ResourceBundle bundle : bundles) {
            for (Enumeration<String> e = bundle.getKeys(); e.hasMoreElements();) {
                String key = e.nextElement();
                merged.setProperty(key, bundle.getString(key));
            }
        }
        try {
            merged.store(stream, "Merged bundles");
            return new PropertyResourceBundle(new ByteArrayInputStream(stream.toByteArray()));
        } catch (Exception e) {
            return EMPTY_BUNDLE;
        }
    }

}
