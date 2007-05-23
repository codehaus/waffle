package org.waffle.taglib.form;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A hashmap implementation of a tags attributes.
 * 
 * @author Guilherme Silveira
 * @since 0.9.5
 */
public class Attributes {

	private Map<String, Object> keys = new HashMap<String,Object>();

	public void put(String localName, Object value) {
		keys.put(localName, value);
	}

	public void outputTo(Writer out) throws IOException {
		for(Entry<String,Object> entry : keys.entrySet()) {
			out.write(" " + entry.getKey() + "=\"" + entry.getValue() + "\" ");
		}
	}

	public String asString() {
		StringBuilder builder = new StringBuilder();
		for(Entry<String,Object> entry : keys.entrySet()) {
			builder.append(" " + entry.getKey() + "=\"" + entry.getValue() + "\" ");
		}
		return builder.toString().trim();
	}

	public void clear() {
		keys.clear();
	}

	public String get(String key) {
		return (String) keys.get(key);
	}

}
