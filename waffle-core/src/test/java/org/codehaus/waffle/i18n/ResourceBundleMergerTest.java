package org.codehaus.waffle.i18n;

import static java.util.Arrays.asList;
import static java.util.ResourceBundle.getBundle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Test;

/**
 * @author Mauro Talevi
 */
public class ResourceBundleMergerTest {

    private static final Locale LOCALE = Locale.getDefault();

    @Test
    public void canMergeMultipleBundles() {
        ResourceBundleMerger merger = new ResourceBundleMerger();
        ResourceBundle bundle = getBundle("Bundle", LOCALE);
        ResourceBundle anotherBundle = getBundle("AnotherBundle", LOCALE);
        List<ResourceBundle> bundles = asList(bundle, anotherBundle);
        ResourceBundle merged = merger.merge(bundles);
        assertEquals(bundle.getString("company"), merged.getString("company"));
        assertEquals(anotherBundle.getString("foo.bar"), merged.getString("foo.bar"));
    }

    @Test
    public void canAllowBundleToOverridePreviousBundles() {
        ResourceBundleMerger merger = new ResourceBundleMerger();
        ResourceBundle bundle = getBundle("Bundle", LOCALE);
        ResourceBundle anotherBundle = getBundle("AnotherBundle", LOCALE);
        List<ResourceBundle> bundles = asList(bundle, anotherBundle);
        ResourceBundle merged = merger.merge(bundles);
        assertEquals(anotherBundle.getString("project.name"), merged.getString("project.name"));
    }

    @Test
    public void canMergeMultipleBundlesToEmptyBundleIfOutputStreamInvalid() {
        ResourceBundleMerger merger = new ResourceBundleMerger(new ByteArrayOutputStream() {
            @Override
            public synchronized byte[] toByteArray() {
                throw new RuntimeException("mock");
            }

        });
        ResourceBundle bundle = getBundle("Bundle", LOCALE);
        ResourceBundle anotherBundle = getBundle("AnotherBundle", LOCALE);
        List<ResourceBundle> bundles = asList(bundle, anotherBundle);
        ResourceBundle merged = merger.merge(bundles);
        assertTrue(merged instanceof EmptyResourceBundle);
    }

}
