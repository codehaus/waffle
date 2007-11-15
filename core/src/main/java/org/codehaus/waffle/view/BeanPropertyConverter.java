package org.codehaus.waffle.view;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Used to make XStream use bean getters to serialize, no attributes
 *
 * @author Paulo Silveira
 * @author Michael Ward
 */
public class BeanPropertyConverter implements Converter {

    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Method readMethod = propertyDescriptor.getReadMethod();

                // skip getClass() method and any get/is methods that take arguments
                if (readMethod.getParameterTypes().length == 0 && !readMethod.getName().equals("getClass")) {
                    writer.startNode(propertyDescriptor.getName());
                    Object got = readMethod.invoke(object);

                    if (got != null) {
                        context.convertAnother(got);
                    }

                    writer.endNode();
                }
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException(e);
        } catch (IntrospectionException e) {
            throw new IllegalStateException(e);
        }
    }

    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader,
                            UnmarshallingContext unmarshallingContext) {
        throw new UnsupportedOperationException("Converter only available for marshaling");
    }

    @SuppressWarnings("unchecked")
    public boolean canConvert(Class clazz) {
        return true;
    }
}
