package org.codehaus.waffle.example.mydvds.action;

import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.FieldErrorMessage;

public class UsersControllerValidator {
    public void add(ErrorsContext errors, String name, String login, String password) {
        if (isEmpty(name)) {
            errors.addErrorMessage(new FieldErrorMessage("name", name, "invalid_name"));
        }
        if (isEmpty(login)) {
            errors.addErrorMessage(new FieldErrorMessage("login", login, "invalid_login"));
        }
        if (isEmpty(password)) {
            errors.addErrorMessage(new FieldErrorMessage("password", password, "invalid_password"));
        }
    }

    private boolean isEmpty(String text) {
        return text == null || text.trim().length() == 0;
    }
}
