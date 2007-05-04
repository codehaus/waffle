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
package com.thoughtworks.waffle.validation;

import java.util.List;

/**
 * ErrorsContext holds error messages of different types
 *
 * @author Mauro Talevi
 * @author Michael Ward
 */
public interface ErrorsContext {

    void addErrorMessage(ErrorMessage message);

    List<ErrorMessage> getAllErrorMessages();

    List<BindErrorMessage> getAllBindErrorMessages();

    List<FieldErrorMessage> getAllFieldErrorMessages();

    List<GlobalErrorMessage> getAllGlobalErrorMessages();

    List<BindErrorMessage> getBindErrorMessages(String fieldName);

    List<FieldErrorMessage> getFieldErrorMessages(String fieldName);

    boolean hasErrorMessages();

    boolean hasBindErrorMessages(String fieldName);

    boolean hasFieldErrorMessages(String fieldName);

    boolean hasGlobalErrorMessages();

    int getErrorMessageCount();

    int getBindErrorMessageCount();

    int getFieldErrorMessageCount();

    int getGlobalErrorMessageCount();

}

