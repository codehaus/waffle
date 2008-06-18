package org.codehaus.waffle.bind.converters;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Locale;

import org.codehaus.waffle.i18n.MessageResourcesConfiguration;

/**
 * @author Mauro Talevi
 */
public abstract class AbstractValueConverterTest {

    protected static final List<Integer> INTEGERS = asList(-1, -2, -3);
    protected static final List<Long> LONGS = asList(1000L, 2000L, 3000L);
    protected static final List<Double> DOUBLES = asList(0.1d, 0.2d, 0.3d);
    protected static final List<Float> FLOATS = asList(0.1f, 0.2f, 0.3f);
    protected static final List<String> STRINGS = asList("one", "two", "three");
    protected static final List<String> MIXED_STRINGS = asList("0#.A", "1#.B");

    protected MessageResourcesConfiguration configuration = new MessageResourcesConfiguration() {

        public Locale getDefaultLocale() {
            return Locale.UK;
        }

        public String getResourceBundleName() {
            return "FakeResourceBundle";
        }

    };

}
