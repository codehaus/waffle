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
 * ViewDataBinder implementation which uses Java beans introspector.
 * RubyControllers are handled separately via IRubyObject instance_variables.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class IntrospectingViewDataBinder implements ViewDataBinder {

    private final BindMonitor bindMonitor;

    public IntrospectingViewDataBinder(BindMonitor bindMonitor) {
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
                    String name = propertyDescriptor.getName();
                    request.setAttribute(name, readMethod.invoke(controller));
                    Object value = request.getAttribute(name);
                    bindMonitor.viewValueBound(name, value, controller);
                }
            }
        } catch (IntrospectionException e) {
            bindMonitor.viewBindFailed(controller, e);
            throw new WaffleException(e);
        } catch (IllegalAccessException e) {
            bindMonitor.viewBindFailed(controller, e);
            throw new WaffleException(e);
        } catch (InvocationTargetException e) {
            bindMonitor.viewBindFailed(controller, e);
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
