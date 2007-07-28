package org.codehaus.waffle.bind;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.controller.RubyController;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * This implementation can handle all standard Java objects and RubyControllers are handled specially (instance_variables)
 */
public class IntrospectingRequestAttributeBinder implements RequestAttributeBinder {

    public void bind(HttpServletRequest request, Object controller) {
        if (controller instanceof RubyController) {
            handleRubyController(request, (RubyController) controller);
            return;
        }

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

    private void handleRubyController(HttpServletRequest request, RubyController rubyController) {
        IRubyObject iRubyObject = rubyController.getRubyObject();
        //noinspection unchecked
        Map<String, IRubyObject> iVars = iRubyObject.getInstanceVariables();
        Set<String> keys = iVars.keySet();

        for (String key : keys) {
            Object value = JavaEmbedUtils.rubyToJava(iRubyObject.getRuntime(), iVars.get(key), Object.class);
            request.setAttribute(key.substring(1), value);
        }
    }
}
