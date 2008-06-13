package org.codehaus.waffle.bind;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.waffle.controller.ScriptedController;
import org.codehaus.waffle.monitor.BindMonitor;

/**
 * Abstract ViewDataBinder implementation for script controllers, delegating 
 * the handling of the script to concrete subclasses, specific for each script.
 * If controller is not a scripted controller, it falls back to using the Java binder.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public abstract class ScriptedViewDataBinder extends IntrospectingViewDataBinder {

    public ScriptedViewDataBinder(BindMonitor bindMonitor) {
        super(bindMonitor);
    }

    public void bind(HttpServletRequest request, Object controller) {
        if (controller instanceof ScriptedController) {
            handleScriptController(request, (ScriptedController) controller);
            return;
        }
        super.bind(request, controller);
    }

    protected abstract void handleScriptController(HttpServletRequest request, ScriptedController controller);

}
