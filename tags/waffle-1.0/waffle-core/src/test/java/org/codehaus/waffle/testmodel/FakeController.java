/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.context.ContextLevel;
import org.codehaus.waffle.view.View;
import org.codehaus.waffle.action.ActionMethodInvocationException;
import org.codehaus.waffle.action.ActionMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FakeController {
    private String name;
    private String[] values;
    private Long numericValue;
    private ContextLevel contextLevel;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(Long numericValue) {
        this.numericValue = numericValue;
    }// used to prove we can set an enum with ognl
    public ContextLevel getContextLevel() {
        return contextLevel;
    }

    public void setContextLevel(ContextLevel contextLevel) {
        this.contextLevel = contextLevel;
    }

    public String passThruMethod(String value) {
        return value;
    }

    public String methodThrowsException(String value) {
        throw new RuntimeException(value);
    }

    public void actionThrowsActionMethodInvocationException(String msg) {
        throw new ActionMethodInvocationException(msg);
    }

    public void actionThrowsActionMethodException() throws ActionMethodException {
        throw new ActionMethodException(0, "blah blah");
    }

    public void sayHello() {
        setName("hello");
    }

    public void sayHello(String value) {
        setName(value);
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public String[] getValues() {
        return values;
    }

    public void sayHelloAlso(StringBuffer sb) {
        setName(sb.toString());
    }

    public void dependsOnRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void dependsOnRequest(HttpServletRequest request, String name) {
        this.request = request;
        this.name = name;
    }

    public void dependsOnSession(HttpSession session) {
        this.session = session;
    }

    public View returnsView(String message) {
        return new View(message, this);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public HttpSession getSession() {
        return session;
    }

    public String toString() {
        return "FakeController: " + name + " hash: " + hashCode();
    }
}
