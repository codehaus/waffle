package org.codehaus.waffle.taglib.form;

import java.util.Map;

/**
 * A combo selection or check box tag based on a collection of items.
 *
 * @author Guilherme Silveira
 * @author Nico Steppat
 */
public class SelectTokensTag extends BasicSelectTag {

    private String tokens;
    private String[] parts;

    @Override
    public void release() {
        super.release();
        tokens = null;
    }

    protected ItemsIterator getItemsIterator() {
        if (tokens == null) {
            return null;
        }
        return new ItemsIterator() {

            private int current = 0;

            public boolean hasNext() {
                return current != parts.length;
            }

            public Map.Entry next() {
                if(!hasNext()) {
                    throw new IllegalStateException("This iterator does not contain any more items");
                }
                Map.Entry entry = new Map.Entry() {
                    int position = current;
                    public Object getKey() {
                        return parts[position];
                    }

                    public Object getValue() {
                        return parts[position+1];
                    }

                    public Object setValue(Object value) {
                        throw new UnsupportedOperationException();
                    }
                };
                current += 2;
                return entry;
            }

        };
    }

    public void setTokens(String tokens) {
        this.tokens = tokens;
        this.parts = tokens.split(",");
        if (this.parts.length % 2 != 0) {
            throw new IllegalArgumentException("String '" + tokens + "' is invalid as there is an odd number of tokens in it.");
        }
    }
}