package org.codehaus.waffle.bind;

import org.codehaus.waffle.WaffleException;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class IntrospectingRequestAttributeBinder implements RequestAttributeBinder {

    public void bind(HttpServletRequest request, Object controller) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(controller.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Method readMethod = propertyDescriptor.getReadMethod();
                // skip getClass() method and any get/is methods that take arguments
                if (readMethod.getParameterTypes().length == 0 && !readMethod.getName().equals("getClass")) {
                    request.setAttribute(propertyDescriptor.getName(), readMethod.invoke(controller));
                }
            }
        } catch (IntrospectionException e) {
            throw new WaffleException(e);
        } catch (IllegalAccessException e) {
            throw new WaffleException(e);
        } catch (InvocationTargetException e) {
            throw new WaffleException(e);
        }
    }
}
