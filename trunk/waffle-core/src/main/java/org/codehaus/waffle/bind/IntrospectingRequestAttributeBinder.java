package org.codehaus.waffle.bind;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.controller.RubyController;
import org.codehaus.waffle.monitor.BindMonitor;
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
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class IntrospectingRequestAttributeBinder implements RequestAttributeBinder {

    private final BindMonitor bindMonitor;

    public IntrospectingRequestAttributeBinder(BindMonitor bindMonitor) {
        this.bindMonitor = bindMonitor;
    }

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

                if(readMethod != null && readMethod.getParameterTypes().length == 0 && !readMethod.getName().equals("getClass")) {
                    request.setAttribute(propertyDescriptor.getName(), readMethod.invoke(controller));
                }
            }
        } catch (IntrospectionException e) {
            bindMonitor.bindFailedForController(controller, e);
            throw new WaffleException(e);
        } catch (IllegalAccessException e) {
            bindMonitor.bindFailedForController(controller, e);
            throw new WaffleException(e);
        } catch (InvocationTargetException e) {
            bindMonitor.bindFailedForController(controller, e);
            throw new WaffleException(e);
        }
    }

    @SuppressWarnings({"unchecked"})
    private void handleRubyController(HttpServletRequest request, RubyController rubyController) {
        IRubyObject iRubyObject = rubyController.getRubyObject();
        Map<String, IRubyObject> iVars = iRubyObject.getInstanceVariables();
        Set<Map.Entry<String, IRubyObject>> entries = iVars.entrySet();

        for (Map.Entry<String, IRubyObject> entry : entries) {
            Object value = JavaEmbedUtils.rubyToJava(iRubyObject.getRuntime(), entry.getValue(), Object.class);
            request.setAttribute(entry.getKey().substring(1), value);
        }
    }
}
