package org.codehaus.waffle.testmodel;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.Type;
import java.util.List;

public class FakeControllerWithListMethods {       
    public static Type methodParameterType(String methodName) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(FakeControllerWithListMethods.class);
        for (MethodDescriptor md : beanInfo.getMethodDescriptors()) {
            if (md.getMethod().getName().equals(methodName)) {
                return md.getMethod().getGenericParameterTypes()[0];
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public void list(List list){};
    public void listOfStrings(List<String> list){};
    public void listOfIntegers(List<Integer> list){};
    public void listOfLongs(List<Integer> list){};
    public void listOfDoubles(List<Integer> list){};
    public void listOfFloats(List<Integer> list){};
    public void object(Object object){};
}