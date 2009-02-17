/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.action.annotation.ActionMethod;
import org.codehaus.waffle.i18n.MessagesContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class FakeControllerWithMethodDefinitions {
    public boolean methodOneInvoked = false;
    public boolean noArgumentMethodInvoked = false;
    public Object methodTwoObject = null;
    public Object methodThreeObject = null;
    public Integer integer = null;
    public Float decimal = null;
    public boolean bool = false;
    public List<String> listOfStrings;
    public List<Integer> listOfIntegers;
    public HttpServletRequest request;
    public HttpServletResponse response;
    public HttpSession session;
    public ServletContext servletContext;
    public MessagesContext messagesContext;

    public void noArgumentMethod() {
        noArgumentMethodInvoked = true;
    }

    public void methodOne() {
        methodOneInvoked = true;
    }

    @ActionMethod(parameters = {"list"})
    public void methodTwo(List<?> list) {
        methodTwoObject = list;
    }

    @ActionMethod(parameters = {"object"})
    public void methodAmbiguous(Object object) {
        methodThreeObject = object;
    }

    @ActionMethod(parameters = {"list"})
    public void methodAmbiguous(List<?> list) {
        methodThreeObject = list;
    }

    @ActionMethod(parameters = {"integer"})
    public void methodInteger(int integer) {
        this.integer = integer;
    }

    @ActionMethod(parameters = {"decimal"})
    public void methodFloat(Float decimal) {
        this.decimal = decimal;
    }

    @ActionMethod(parameters = {"bool"})
    public void methodBoolean(boolean bool) {
        this.bool = bool;
    }
    
    @ActionMethod(parameters = {"list"})
    public void methodListOfStrings(List<String> list) {
        this.listOfStrings = list;
    }
    
    @ActionMethod(parameters = {"list"})
    public void methodListOfIntegers(List<Integer> list) {
        this.listOfIntegers = list;
    }


    @ActionMethod
    public void methodDependsOnRequest(HttpServletRequest request) {
        this.request = request;
    }

    @ActionMethod
    public void methodDependsOnResponse(HttpServletResponse response) {
        this.response = response;
    }

    @ActionMethod
    public void methodDependsOnSession(HttpSession session) {
        this.session = session;
    }

    @ActionMethod
    public void methodDependsOnMessagesContext(MessagesContext messagesContext) {
        this.messagesContext = messagesContext;
    }

    @ActionMethod
    public void methodDependsOnServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @ActionMethod(parameters = {"integer"})
    public void methodDependsOnRequestAndInteger(HttpServletRequest request, int integer) {
        this.request = request;
        this.integer = integer;
    }

    @ActionMethod(parameters = {"foobaz"})
    public void actionMethodNeedsCustomConverter(List<?> list) {

    }

    public void noAmbiguityWhenMethodNotPublic(HttpServletRequest request) {

    }

    protected void noAmbiguityWhenMethodNotPublic(HttpServletResponse response) {

    }

}
