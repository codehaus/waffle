/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.validation;

import java.util.List;

import org.codehaus.waffle.validation.ErrorMessage.Type;

/**
 * ErrorsContext holds error messages of different types
 *
 * @author Mauro Talevi
 * @author Michael Ward
 */
public interface ErrorsContext {

    void addErrorMessage(ErrorMessage message);

    List<ErrorMessage> getAllErrorMessages();

    List<? extends ErrorMessage> getErrorMessagesOfType(Type type);

    List<? extends ErrorMessage> getErrorMessagesForField(Type type, String fieldName);

    boolean hasErrorMessages();

    boolean hasErrorMessagesOfType(Type type);

    boolean hasErrorMessagesForField(Type type, String fieldName);

    int getErrorMessageCount();   

    int getErrorMessageCountOfType(Type type);
    
    int getErrorMessageCountForField(Type type, String fieldName);

    void clearErrorMessages();
    
}

