package org.codehaus.waffle.action;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.Type;
import java.util.List;

import org.codehaus.waffle.action.annotation.ActionMethod;

/**
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public abstract class AbstractMethodDefinitionFinderTest {

    protected Type parameterTypeForMethod(Class<?> controllerClass, String methodName) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(controllerClass);
        for ( MethodDescriptor md : beanInfo.getMethodDescriptors() ){
            if ( md.getMethod().getName().equals(methodName) ){
                return md.getMethod().getGenericParameterTypes()[0];
            }
        }
        return null;
    }

    protected static interface ControllerWithListMethods {
        void listOfIntegers(List<Integer> list);
        void listOfStrings(List<String> list);
    }
       
    protected static class ControllerWithDefaultActionMethod {

        @ActionMethod(asDefault=true, parameters = { "helloworld" })
        public void foobar(String value) {

        }
    }

    protected static class ControllerWithDefaultActionMethodNoValue {

        @ActionMethod(asDefault=true)
        public void foobar() {

        }
    }
}
