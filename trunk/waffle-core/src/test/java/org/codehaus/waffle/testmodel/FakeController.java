/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.testmodel;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.waffle.action.ActionMethodException;
import org.codehaus.waffle.action.ActionMethodInvocationException;
import org.codehaus.waffle.view.View;

public class FakeController {
    private String name;
    private String[] values;
    private List<String> list;
    private Number number;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Number getNumber() {
        return number;
    }

    public void setNumber(Number number) {
        this.number = number;
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

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
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
        return new View(message);
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
        return "[FakeController name: " + name + ", list: "+list+", hash: " + hashCode()+"]";
    }
}
