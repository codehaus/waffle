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
public class AbstractMethodDefinitionFinderTest {


    protected Type parameterTypeForMethod(String methodName) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(WithListMethods.class);
        for ( MethodDescriptor md : beanInfo.getMethodDescriptors() ){
            if ( md.getMethod().getName().equals(methodName) ){
                return md.getMethod().getGenericParameterTypes()[0];
            }
        }
        return null;
    }

    protected static interface WithListMethods {
        void listOfIntegers(List<Integer> list);
        void listOfStrings(List<String> list);
    }
    
   
    public class ControllerWithDefaultActionMethod {

        @ActionMethod(asDefault=true, parameters = { "helloworld" })
        public void foobar(String value) {

        }
    }

    public class ControllerWithDefaultActionMethodNoValue {

        @ActionMethod(asDefault=true)
        public void foobar() {

        }
    }
}
