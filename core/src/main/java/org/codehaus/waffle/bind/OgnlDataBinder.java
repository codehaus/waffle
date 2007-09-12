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
package org.codehaus.waffle.bind;

import org.codehaus.waffle.validation.BindErrorMessage;
import org.codehaus.waffle.validation.ErrorsContext;
import ognl.InappropriateExpressionException;
import ognl.NoSuchPropertyException;
import ognl.Ognl;
import ognl.OgnlException;
import ognl.TypeConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;

/**
 * This DataBinder implementation is backed by <a href="http://www.ognl.org">Ognl: Object Graph Notation Language</a>.
 *
 * @author Michael Ward
 */
public class OgnlDataBinder implements DataBinder {
    private final TypeConverter typeConverter;
    private final BindErrorMessageResolver bindErrorMessageResolver;

    public OgnlDataBinder(TypeConverter typeConverter, BindErrorMessageResolver bindErrorMessageResolver) {
        this.typeConverter = typeConverter;
        this.bindErrorMessageResolver = bindErrorMessageResolver;
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
                errorsContext.addErrorMessage(new BindErrorMessage(name, value, message));
            } catch (BindException e) {
                // by convention BindExceptions should provide the correct bind error message to display to the end-user
                errorsContext.addErrorMessage(new BindErrorMessage(name, value, e.getMessage()));
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
