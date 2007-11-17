package org.codehaus.waffle.taglib.form;

import org.codehaus.waffle.taglib.Functions;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * A combo selection or check box tag based on a collection of items.
 *
 * @author Guilherme Silveira
 * @author Nico Steppat
 */
public class SelectTag extends BasicSelectTag {
    private Collection<Object> items;
    private String value;

    @Override
    public void release() {
        super.release();
        items = null;
        value = null;
    }

    protected ItemsIterator getItemsIterator() {
        if (items == null) {
            return null;
        }
        return new ItemsIterator() {
            private Iterator iterator = items.iterator();

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public Map.Entry next() {
                if (!hasNext()) {
                    throw new IllegalStateException("This iterator does not contain any more items");
                }
                return new Map.Entry() {
                    Object current = iterator.next();

                    public Object getKey() {
                        return Functions.getProperty(current, value);
                    }

                    public Object getValue() {
                        return current;
                    }

                    public Object setValue(Object value) {
                        throw new UnsupportedOperationException();
                    }
                };
            }

        };
    }

    public void setItems(Collection<Object> collection) {
        this.items = collection;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
