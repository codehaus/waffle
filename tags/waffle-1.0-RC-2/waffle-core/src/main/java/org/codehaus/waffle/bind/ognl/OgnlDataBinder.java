/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
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
import org.codehaus.waffle.bind.DataBinder;
import org.codehaus.waffle.bind.ValueConverterFinder;
import org.codehaus.waffle.monitor.BindMonitor;
import org.codehaus.waffle.validation.BindErrorMessage;
import org.codehaus.waffle.validation.ErrorsContext;

/**
 * This DataBinder implementation is backed by <a href="http://www.ognl.org">Ognl: Object Graph Notation Language</a>.
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class OgnlDataBinder implements DataBinder {
    private final TypeConverter typeConverter;
    private final BindErrorMessageResolver bindErrorMessageResolver;
    private final BindMonitor bindMonitor;

    public OgnlDataBinder(ValueConverterFinder valueConverterFinder, BindErrorMessageResolver bindErrorMessageResolver, BindMonitor bindMonitor) {
        this.typeConverter = new DelegatingTypeConverter(valueConverterFinder);
        this.bindErrorMessageResolver = bindErrorMessageResolver;
        this.bindMonitor = bindMonitor;
    }
    
    @SuppressWarnings({"unchecked"})
    public void bind(HttpServletRequest request, HttpServletResponse response, ErrorsContext errorsContext, Object model) {
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = request.getParameter(name);

            try {
                handleConvert(name, value, model);
            } catch (OgnlException e) {
                String message = bindErrorMessageResolver.resolve(model, name, value);
                BindErrorMessage errorMessage = new BindErrorMessage(name, value, message);
                errorsContext.addErrorMessage(errorMessage);
                bindMonitor.bindFailedForModel(model, errorMessage);                
            } catch (BindException e) {
                // by convention BindExceptions should provide the correct bind error message to display to the end-user
                BindErrorMessage errorMessage = new BindErrorMessage(name, value, e.getMessage());
                errorsContext.addErrorMessage(errorMessage);
                bindMonitor.bindFailedForModel(model, errorMessage);
            }
        }
    }

    protected void handleConvert(String propertyName,
                                 String parameterValue,
                                 Object model) throws OgnlException, BindException {
        try {
            Object tree = Ognl.parseExpression(propertyName);
            Map ognlContext = Ognl.createDefaultContext(model);
            Ognl.setTypeConverter(ognlContext, typeConverter);
            Ognl.setValue(tree, ognlContext, model, parameterValue);
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
    }

}
