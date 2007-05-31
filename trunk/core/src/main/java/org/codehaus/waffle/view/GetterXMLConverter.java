package org.codehaus.waffle.view;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.TreeMap;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Used to make XStream use bean getters to serialize, no attributes
 * @author Paulo Silveira
 *
 */
public class GetterXMLConverter implements Converter {

    private static final String IS_INITIALS = "is";

    private static final String GET_INITIALS = "get";

    private static final Object[] NO_ARGUMENTS = {};

    public void marshal(Object o, HierarchicalStreamWriter writer,
            MarshallingContext context) {

        Map<String, Method> getters = getGetters(o.getClass());
        for (String name : getters.keySet()) {
            Method getter = getters.get(name);
            writer.startNode(name);
            try {
                Object got = getter.invoke(o, NO_ARGUMENTS);
                if (got != null)

                    context.convertAnother(got);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException(e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
            writer.endNode();

        }
    }

    public Object unmarshal(HierarchicalStreamReader arg0,
            UnmarshallingContext arg1) {
        throw new UnsupportedOperationException(
                "Converter only available for marshaling");
    }

    public boolean canConvert(Class clazz) {
        return true;
    }

    public static Map<String, Method> getGetters(Class clazz) {
        Map<String, Method> methods = new TreeMap<String, Method>();
        for (Method m : clazz.getMethods()) {
            if (!isGetter(m)) {
                continue;
            }
            if (m.getDeclaringClass().equals(Object.class)) {
                // hack: removing getClass()
                continue;
            }
            String propertyName = "";
            if (m.getName().startsWith(GET_INITIALS)) {
                propertyName = m.getName().substring(GET_INITIALS.length());

            } else if (m.getName().startsWith(IS_INITIALS)) {
                propertyName = m.getName().substring(IS_INITIALS.length());
            }
            // ok, this is a hack, cause we can have a problem
            // with classes with a get() method
            // (the propertyname would be an empty string)
            if (propertyName.length() != 0) {
                if (propertyName.length() == 1
                        || Character.isLowerCase(propertyName.charAt(1))) {
                    propertyName = decapitalize(propertyName);
                }
                methods.put(propertyName, m);
            }
        }
        return methods;
    }

    private static String decapitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1))
                && Character.isUpperCase(name.charAt(0))) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    public static boolean isGetter(Method m) {
        if (m.getParameterTypes().length != 0
                || !Modifier.isPublic(m.getModifiers())
                || m.getReturnType().equals(Void.TYPE)) {
            return false;
        }
        if (Modifier.isStatic(m.getModifiers())
                || !Modifier.isPublic(m.getModifiers())
                || Modifier.isAbstract(m.getModifiers())) {
            return false;
        }
        if (m.getName().startsWith(GET_INITIALS)
                && m.getName().length() > GET_INITIALS.length()) {
            return true;
        }
        if (m.getName().startsWith(IS_INITIALS)
                && m.getName().length() > IS_INITIALS.length()
                && (m.getReturnType().equals(boolean.class) || m
                        .getReturnType().equals(Boolean.class))) {
            return true;
        }
        return false;
    }

}
