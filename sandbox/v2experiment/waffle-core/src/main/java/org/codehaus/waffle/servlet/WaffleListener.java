package org.codehaus.waffle.servlet;

import org.codehaus.waffle.context.pico.WaffleLifecycleStrategy;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.LifecycleStrategy;
import org.picocontainer.behaviors.Caching;
import org.picocontainer.behaviors.Guarding;
import org.picocontainer.behaviors.Storing;
import org.picocontainer.web.PicoServletContainerListener;

@SuppressWarnings("serial")
public class WaffleListener extends PicoServletContainerListener {

    protected ScopedContainers makeScopedContainers() {
        DefaultPicoContainer application = new DefaultPicoContainer(new Guarding().wrap(new Caching()), makeLifecycleStrategy(), makeParentContainer());
        Storing sessionStoring = new Storing();
        DefaultPicoContainer session = new DefaultPicoContainer(new Guarding().wrap(sessionStoring), makeLifecycleStrategy(), application);
        Storing requestStoring = new Storing();
        DefaultPicoContainer request = new DefaultPicoContainer(new Guarding().wrap(addRequestBehaviors(requestStoring)), makeLifecycleStrategy(), session);
        return new ScopedContainers(application, session, request, sessionStoring, requestStoring);
    }

    protected LifecycleStrategy makeLifecycleStrategy() {
        return new WaffleLifecycleStrategy(makeComponentMonitor());
    }


}
