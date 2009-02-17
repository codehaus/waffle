package org.codehaus.waffle.testing;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauro Talevi
 */
public class MapController implements Controller {
    private Map<MapKey, String> names;

    public Map<MapKey, String> getNames() {
        return names;
    }

    public void process() {
        names = new HashMap<MapKey, String>();
        names.put(new MapKey("one"), "One");
        names.put(new MapKey("two"), "Two");
    }

    public static class MapKey {
        private final String key;

        public MapKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        @Override
        public String toString() {
            return key;
        }
    }
}
