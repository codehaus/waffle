package org.codehaus.waffle.taglib.acceptance;

import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.FieldErrorMessage;
import org.codehaus.waffle.validation.GlobalErrorMessage;

/**
 * @Author Fabio Kung
 */
public class ProductsControllerValidator {
    public void add(ErrorsContext errors, String name, Double price) {
        boolean hasError = false;
        if (price == null || price.doubleValue() == 0.0) {
            hasError = true;
            errors.addErrorMessage(new FieldErrorMessage("price", price.toString(), "invalid_price"));
        }
        if (name == null || name.trim().length() == 0) {
            hasError = true;
            errors.addErrorMessage(new FieldErrorMessage("name", name, "invalid_name"));
        }
        if (hasError) {
            errors.addErrorMessage(new GlobalErrorMessage("there are errors..."));
        }
    }
}
