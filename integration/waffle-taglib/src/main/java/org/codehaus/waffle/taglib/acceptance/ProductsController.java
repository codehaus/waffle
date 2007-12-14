package org.codehaus.waffle.taglib.acceptance;

import org.codehaus.waffle.action.annotation.ActionMethod;
import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.FieldErrorMessage;
import org.codehaus.waffle.validation.GlobalErrorMessage;

/**
 * @Author Fabio Kung
 */
public class ProductsController {

    @ActionMethod(parameters = {"name", "price"})
    public void add(String name, Double price) {
        // has validation errors...
    }
}
