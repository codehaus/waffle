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
package org.codehaus.waffle.validation;

import org.codehaus.waffle.Constants;
import org.codehaus.waffle.Startable;
import org.codehaus.waffle.validation.ErrorMessage.Type;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of ErrorsContext.
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class DefaultErrorsContext implements ErrorsContext, Startable {
    private final HttpServletRequest request;
    private final Map<String, List<BindErrorMessage>> bindErrorMessages = new HashMap<String, List<BindErrorMessage>>(); // todo this should only have one bind per field MAX
    private final Map<String, List<FieldErrorMessage>> fieldErrorMessages = new HashMap<String, List<FieldErrorMessage>>();
    private final List<GlobalErrorMessage> globaErrorMessages = new ArrayList<GlobalErrorMessage>();

    public DefaultErrorsContext(HttpServletRequest request) {
        this.request = request;
    }

    public void addErrorMessage(ErrorMessage message) {
        switch ( message.getType() ){
            case BIND: addBindValidationMessage((BindErrorMessage) message);
                break;
            case FIELD: addFieldValidationMessage((FieldErrorMessage) message);
                break;
            default: 
                addGlobalValidationMessage((GlobalErrorMessage) message);
        }
    }

    private void addBindValidationMessage(BindErrorMessage bindValidationMessage) {
        String name = bindValidationMessage.getName();
        List<BindErrorMessage> messages = bindErrorMessages.get(name);

        if (messages == null) {
            messages = new ArrayList<BindErrorMessage>(10);
            bindErrorMessages.put(name, messages);
        }

        messages.add(bindValidationMessage);
    }

    private void addFieldValidationMessage(FieldErrorMessage fieldValidationMessage) {
        String name = fieldValidationMessage.getName();
        List<FieldErrorMessage> messages = fieldErrorMessages.get(name);

        if (messages == null) {
            messages = new ArrayList<FieldErrorMessage>(10);
            fieldErrorMessages.put(name, messages);
        }

        messages.add(fieldValidationMessage);
    }

    private void addGlobalValidationMessage(GlobalErrorMessage message) {
        globaErrorMessages.add(message);
    }

    public List<ErrorMessage> getAllErrorMessages() {
        List<ErrorMessage> messages = new ArrayList<ErrorMessage>();
        messages.addAll(getAllBindErrorMessages());
        messages.addAll(getAllFieldErrorMessages());
        messages.addAll(getAllGlobalErrorMessages());
        return messages;
    }

    public List<? extends ErrorMessage> getErrorMessagesOfType(Type type) {
        switch ( type ){
            case BIND: return getAllBindErrorMessages();
            case FIELD: return getAllFieldErrorMessages();
            case GLOBAL: return getAllGlobalErrorMessages();
            default: return Collections.emptyList();
        }
    }

    private List<BindErrorMessage> getAllBindErrorMessages() {
        List<BindErrorMessage> messages = new ArrayList<BindErrorMessage>();

        for (List<BindErrorMessage> bindMessages : bindErrorMessages.values()) {
            messages.addAll(bindMessages);
        }

        return messages;
    }

    private List<FieldErrorMessage> getAllFieldErrorMessages() {
        List<FieldErrorMessage> messages = new ArrayList<FieldErrorMessage>();

        for (List<FieldErrorMessage> fieldMessages : fieldErrorMessages.values()) {
            messages.addAll(fieldMessages);
        }

        return messages;
    }

    private List<GlobalErrorMessage> getAllGlobalErrorMessages() {
        return globaErrorMessages;
    }

    public List<? extends ErrorMessage> getErrorMessagesForField(Type type, String fieldName) {
        switch ( type ){
            case BIND: return getBindErrorMessages(fieldName);
            case FIELD: return getFieldErrorMessages(fieldName);
            case GLOBAL: return Collections.emptyList();
            default: return Collections.emptyList();
        }
    }

    private List<BindErrorMessage> getBindErrorMessages(String fieldName) {
        List<BindErrorMessage> list = bindErrorMessages.get(fieldName);

        if(list == null) {
            list = Collections.emptyList();
        }

        return list;
    }

    private List<FieldErrorMessage> getFieldErrorMessages(String fieldName) {
        List<FieldErrorMessage> list = fieldErrorMessages.get(fieldName);

        if(list == null) {
            list = Collections.emptyList();
        }

        return list;
    }

    public boolean hasErrorMessages() {
        return hasBindErrorMessages() || hasFieldErrorMessages() || hasGlobalErrorMessages();
    }

    public boolean hasErrorMessagesOfType(Type type) {
        switch ( type ){
            case BIND: return hasBindErrorMessages();
            case FIELD: return hasFieldErrorMessages();
            case GLOBAL: return hasGlobalErrorMessages();
            default: return false;
        }
    }

    private boolean hasBindErrorMessages() {
        return !bindErrorMessages.isEmpty();
    }

    private boolean hasFieldErrorMessages() {
        return !fieldErrorMessages.isEmpty();
    }

    private boolean hasGlobalErrorMessages() {
        return !globaErrorMessages.isEmpty();
    }

    public boolean hasErrorMessagesForField(Type type, String fieldName) {
        switch ( type ){
            case BIND: return hasBindErrorMessages(fieldName);
            case FIELD: return hasFieldErrorMessages(fieldName);
            case GLOBAL: return hasGlobalErrorMessages();
            default: return false;
        }
    }

    private boolean hasBindErrorMessages(String fieldName) {
        return bindErrorMessages.containsKey(fieldName);
    }

    private boolean hasFieldErrorMessages(String fieldName) {
        return fieldErrorMessages.containsKey(fieldName);
    }

    public int getErrorMessageCount() {
        return getBindErrorMessageCount() + getFieldErrorMessageCount() + getGlobalErrorMessageCount();
    }
    
    public int getErrorMessageCountOfType(Type type) {
        switch ( type ){
            case BIND: return getBindErrorMessageCount();
            case FIELD: return getFieldErrorMessageCount();
            case GLOBAL: return getGlobalErrorMessageCount();                    
        }
        return 0;
    }
    
    private int getBindErrorMessageCount() {
        return bindErrorMessages.size();
    }

    private int getFieldErrorMessageCount() {
        return fieldErrorMessages.size();
    }

    private int getGlobalErrorMessageCount() {
        return globaErrorMessages.size();
    }

    public int getErrorMessageCountForField(Type type, String fieldName) {
        switch ( type ){
            case BIND: return getBindErrorMessageCount(fieldName);
            case FIELD: return getFieldErrorMessageCount(fieldName);
            case GLOBAL: return getGlobalErrorMessageCount();                    
        }
        return 0;
    }

    private int getBindErrorMessageCount(String fieldName) {
        if ( hasBindErrorMessages(fieldName) ){
            return getBindErrorMessages(fieldName).size();
        }
        return 0;
    }

    private int getFieldErrorMessageCount(String fieldName) {
        if ( hasFieldErrorMessages(fieldName) ){
            return getFieldErrorMessages(fieldName).size();
        }
        return 0;
    }

    public void clearErrorMessages() {
        bindErrorMessages.clear();
        fieldErrorMessages.clear();
        globaErrorMessages.clear();        
    }

    public void start() {
        request.setAttribute(Constants.ERRORS_KEY, this);
    }

    public void stop() {
        /// does nothing
    }
}
