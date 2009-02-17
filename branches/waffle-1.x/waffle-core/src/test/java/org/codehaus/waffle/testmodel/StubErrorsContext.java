package org.codehaus.waffle.testmodel;

import java.util.List;

import org.codehaus.waffle.validation.ErrorMessage;
import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.ErrorMessage.Type;

public class StubErrorsContext implements ErrorsContext {

    public void addErrorMessage(ErrorMessage message) {
    }

    public List<ErrorMessage> getAllErrorMessages() {
        return null;
    }

    public int getErrorMessageCount() {        
        return 0;
    }

    public int getErrorMessageCountForField(Type type, String fieldName) {
        return 0;
    }

    public int getErrorMessageCountOfType(Type type) {
        return 0;
    }

    public List<? extends ErrorMessage> getErrorMessagesForField(Type type, String fieldName) {
        return null;
    }

    public List<? extends ErrorMessage> getErrorMessagesOfType(Type type) {
        return null;
    }

    public boolean hasErrorMessages() {
        return false;
    }

    public boolean hasErrorMessagesForField(Type type, String fieldName) {
        return false;
    }

    public boolean hasErrorMessagesOfType(Type type) {
        return false;
    }

    public void clearErrorMessages() {
    }
    
}
