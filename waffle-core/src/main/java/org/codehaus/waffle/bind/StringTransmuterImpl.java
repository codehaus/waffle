package org.codehaus.waffle.bind;

public class StringTransmuterImpl implements StringTransmuter {
    private final ValueConverterFinder valueConverterFinder;

    public StringTransmuterImpl(ValueConverterFinder valueConverterFinder) {
        this.valueConverterFinder = valueConverterFinder;
    }

    public <T> T transmute(String value, Class<T> toType) {
        if (isEmpty(value) && toType.isPrimitive()) {
            value = null; // this allows Ognl to use that primitives default value
        }
        return valueConverterFinder.findConverter(toType).convertValue(null, value, toType);
    }

    private boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }
}
