package org.codehaus.waffle.testmodel;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.Type;

public class FakeControllerWithNumberMethods {       
    public static Type methodParameterType(String methodName) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(FakeControllerWithNumberMethods.class);
        for (MethodDescriptor md : beanInfo.getMethodDescriptors()) {
            if (md.getMethod().getName().equals(methodName)) {
                return md.getMethod().getGenericParameterTypes()[0];
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public void primitiveDouble(double number){};
    public void primitiveFloat(float number){};
    public void primitiveLong(long number){};
    public void primitiveInteger(int number){};
    public void object(Object object){};
}