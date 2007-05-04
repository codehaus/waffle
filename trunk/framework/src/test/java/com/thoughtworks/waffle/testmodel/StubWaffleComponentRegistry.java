package com.thoughtworks.waffle.testmodel;

import com.thoughtworks.waffle.action.ControllerDefinitionFactory;
import com.thoughtworks.waffle.bind.DataBinder;
import com.thoughtworks.waffle.bind.OgnlDataBinder;
import com.thoughtworks.waffle.context.ContextContainerFactory;
import com.thoughtworks.waffle.servlet.PicoWaffleComponentRegistry;
import com.thoughtworks.waffle.action.method.ActionMethodResponse;
import com.thoughtworks.waffle.action.method.ActionMethodExecutor;
import com.thoughtworks.waffle.action.method.ActionMethodResponseHandler;
import ognl.DefaultTypeConverter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StubWaffleComponentRegistry extends PicoWaffleComponentRegistry {

    public StubWaffleComponentRegistry(ServletContext servletContext) {
        super(servletContext);
    }

    public ActionMethodResponseHandler getActionMethodResponseHandler() {
        return new ActionMethodResponseHandler() {

            public void handle(HttpServletRequest request,
                               HttpServletResponse response,
                               ActionMethodResponse actionMethodResponse) {
                // does nothing
            }
        };
    }

    public DataBinder getDataBinder() {
        return new OgnlDataBinder(new DefaultTypeConverter(), null);
    }

    public ControllerDefinitionFactory getControllerDefinitionFactory() {
        return null;
    }

    public ContextContainerFactory getContextContainerFactory() {
        return null;
    }

    public ActionMethodExecutor getActionMethodExecutor() {
        return new StubActionMethodExecutor();
    }
}
