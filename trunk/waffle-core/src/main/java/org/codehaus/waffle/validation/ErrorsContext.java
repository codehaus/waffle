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

