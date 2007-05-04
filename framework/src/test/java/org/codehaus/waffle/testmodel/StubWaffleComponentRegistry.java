package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.action.ControllerDefinitionFactory;
import org.codehaus.waffle.bind.DataBinder;
import org.codehaus.waffle.bind.OgnlDataBinder;
import org.codehaus.waffle.context.ContextContainerFactory;
import org.codehaus.waffle.servlet.PicoWaffleComponentRegistry;
import org.codehaus.waffle.action.method.ActionMethodResponse;
import org.codehaus.waffle.action.method.ActionMethodExecutor;
import org.codehaus.waffle.action.method.ActionMethodResponseHandler;
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
