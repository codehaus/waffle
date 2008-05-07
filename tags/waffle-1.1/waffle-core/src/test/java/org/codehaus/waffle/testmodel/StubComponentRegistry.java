package org.codehaus.waffle.testmodel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.action.ActionMethodExecutor;
import org.codehaus.waffle.action.ActionMethodResponse;
import org.codehaus.waffle.action.ActionMethodResponseHandler;
import org.codehaus.waffle.bind.DataBinder;
import org.codehaus.waffle.bind.ognl.OgnlDataBinder;
import org.codehaus.waffle.bind.ognl.OgnlValueConverterFinder;
import org.codehaus.waffle.context.ContextContainerFactory;
import org.codehaus.waffle.context.pico.PicoComponentRegistry;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;
import org.codehaus.waffle.monitor.SilentMonitor;

public class StubComponentRegistry extends PicoComponentRegistry {

    public StubComponentRegistry(ServletContext servletContext) {
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
        return new OgnlDataBinder(new OgnlValueConverterFinder(), null, new SilentMonitor());
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
