/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind.ognl;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ognl.InappropriateExpressionException;
import ognl.NoSuchPropertyException;
import ognl.Ognl;
import ognl.OgnlException;
import ognl.TypeConverter;

import org.codehaus.waffle.bind.BindErrorMessageResolver;
import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.bind.ControllerDataBinder;
import org.codehaus.waffle.bind.ValueConverterFinder;
import org.codehaus.waffle.monitor.BindMonitor;
import org.codehaus.waffle.validation.BindErrorMessage;
import org.codehaus.waffle.validation.ErrorsContext;

/**
 * ControllerDataBinder implementation backed by <a href="http://www.ognl.org">Ognl: Object Graph Notation Language</a>.
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class OgnlControllerDataBinder implements ControllerDataBinder {
    private final TypeConverter typeConverter;
    private final BindErrorMessageResolver bindErrorMessageResolver;
    private final BindMonitor bindMonitor;

    public OgnlControllerDataBinder(ValueConverterFinder valueConverterFinder, BindErrorMessageResolver bindErrorMessageResolver, BindMonitor bindMonitor) {
        this.typeConverter = new DelegatingTypeConverter(valueConverterFinder);
        this.bindErrorMessageResolver = bindErrorMessageResolver;
        this.bindMonitor = bindMonitor;
    }
    
    @SuppressWarnings({"unchecked"})
    public void bind(HttpServletRequest request, HttpServletResponse response, ErrorsContext errorsContext, Object controller) {
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();           
            String parameterValue = getParameterValue(request, parameterName);

            try {
                Object dataValue = handleConvert(parameterName, parameterValue, controller);
                bindMonitor.controllerValueBound(parameterName, dataValue, controller);
            } catch (OgnlException e) {
                String message = bindErrorMessageResolver.resolve(controller, parameterName, parameterValue);
                BindErrorMessage errorMessage = new BindErrorMessage(parameterName, parameterValue, message, e);
                errorsContext.addErrorMessage(errorMessage);
                bindMonitor.controllerBindFailed(controller, errorMessage, e);                
            } catch (BindException e) {
                // by convention BindExceptions should provide the correct bind error message to display to the end-user
                BindErrorMessage errorMessage = new BindErrorMessage(parameterName, parameterValue, e.getMessage(), e);
                errorsContext.addErrorMessage(errorMessage);
                bindMonitor.controllerBindFailed(controller, errorMessage, e);
            }
        }
    }

    private String getParameterValue(HttpServletRequest request, String name) {
        // Look for multiple values and join them if found
        String[] values = request.getParameterValues(name);
        if ( values == null ){
            return null;
        }
        // Joining a single value will return the equivalent of request.getParameter(name)
        return join(values, ",");
    }

    // Could use commons-lang StringUtils.join() but avoid introducing a dependency for such a trivial operation
    private String join(String[] values, String separator) {
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < values.length; i++ ){
            sb.append(values[i]);
            if ( i < values.length - 1 ){
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    protected Object handleConvert(String propertyName,
                                 String parameterValue,
                                 Object controller) throws OgnlException, BindException {
        try {
            Object tree = Ognl.parseExpression(propertyName);
            Map ognlContext = Ognl.createDefaultContext(controller);
            Ognl.setTypeConverter(ognlContext, typeConverter);
            Ognl.setValue(tree, ognlContext, controller, parameterValue);
            return Ognl.getValue(tree, ognlContext, controller);
        } catch (NoSuchPropertyException ignore) {
            // ignore NoSuchPropertyException
        } catch (InappropriateExpressionException ignore) {
            // ignore InappropriateExpressionException
        } catch (OgnlException e) {
            if (e.getReason() instanceof BindException) {
                throw (BindException) e.getReason();
            }
            throw e;
        }
        return parameterValue;
    }

}
