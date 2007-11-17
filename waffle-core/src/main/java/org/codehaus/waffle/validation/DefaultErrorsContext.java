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
package org.codehaus.waffle.validation;

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
public class DefaultErrorsContext implements ErrorsContext {
    private final Map<String, List<BindErrorMessage>> bindErrorMessages = new HashMap<String, List<BindErrorMessage>>(); // todo this should only have one bind per field MAX
    private final Map<String, List<FieldErrorMessage>> fieldErrorMessages = new HashMap<String, List<FieldErrorMessage>>();
    private final List<GlobalErrorMessage> globaErrorlMessages = new ArrayList<GlobalErrorMessage>();

    public void addErrorMessage(ErrorMessage message) {
        if (message instanceof BindErrorMessage) {
            addBindValidationMessage((BindErrorMessage) message);
        } else if (message instanceof FieldErrorMessage) {
            addFieldValidationMessage((FieldErrorMessage) message);
        } else {
            addGlobalValidationMessage((GlobalErrorMessage) message);
        }
    }

    public List<ErrorMessage> getAllErrorMessages() {
        List<ErrorMessage> messages = new ArrayList<ErrorMessage>();
        messages.addAll(getAllBindErrorMessages());
        messages.addAll(getAllFieldErrorMessages());
        messages.addAll(getAllGlobalErrorMessages());
        return messages;
    }

    public List<BindErrorMessage> getAllBindErrorMessages() {
        List<BindErrorMessage> messages = new ArrayList<BindErrorMessage>();

        for (List<BindErrorMessage> bindValidationMessages : bindErrorMessages.values()) {
            messages.addAll(bindValidationMessages);
        }

        return messages;
    }

    public List<FieldErrorMessage> getAllFieldErrorMessages() {
        List<FieldErrorMessage> messages = new ArrayList<FieldErrorMessage>();

        for (List<FieldErrorMessage> fieldValidationMessages : fieldErrorMessages.values()) {
            messages.addAll(fieldValidationMessages);
        }

        return messages;
    }

    public List<GlobalErrorMessage> getAllGlobalErrorMessages() {
        return globaErrorlMessages;
    }

    public List<BindErrorMessage> getBindErrorMessages(String fieldName) {
        List<BindErrorMessage> list = bindErrorMessages.get(fieldName);

        if(list == null) {
            list = Collections.emptyList();
        }

        return list;
    }

    public List<FieldErrorMessage> getFieldErrorMessages(String fieldName) {
        List<FieldErrorMessage> list = fieldErrorMessages.get(fieldName);

        if(list == null) {
            list = Collections.emptyList();
        }

        return list;
    }

    public boolean hasErrorMessages() {
        return !fieldErrorMessages.isEmpty() || !bindErrorMessages.isEmpty() || !globaErrorlMessages.isEmpty();
    }

    public boolean hasBindErrorMessages(String fieldName) {
        return bindErrorMessages.containsKey(fieldName);
    }

    public boolean hasFieldErrorMessages(String fieldName) {
        return fieldErrorMessages.containsKey(fieldName);
    }

    public boolean hasGlobalErrorMessages() {
        return !globaErrorlMessages.isEmpty();
    }

    public int getErrorMessageCount() {
        return bindErrorMessages.size() + fieldErrorMessages.size() + globaErrorlMessages.size();
    }

    public int getBindErrorMessageCount() {
        return bindErrorMessages.size();
    }

    public int getFieldErrorMessageCount() {
        return fieldErrorMessages.size();
    }

    public int getGlobalErrorMessageCount() {
        return globaErrorlMessages.size();
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
        globaErrorlMessages.add(message);
    }

}
