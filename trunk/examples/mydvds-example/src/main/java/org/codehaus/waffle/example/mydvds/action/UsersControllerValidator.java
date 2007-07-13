package org.codehaus.waffle.example.mydvds.action;

import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.FieldErrorMessage;
import org.codehaus.waffle.view.View;

public class UsersControllerValidator {
    public View add(ErrorsContext errors, String name, String login, String password) {
        errors.addErrorMessage(new FieldErrorMessage("user.login", login, "invalid"));
        return new View("index.jspx",this);
    }
}
