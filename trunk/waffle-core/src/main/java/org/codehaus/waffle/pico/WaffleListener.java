package org.codehaus.waffle.pico;

import org.codehaus.waffle.pico.WaffleLifecycleStrategy;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.behaviors.Caching;
import org.picocontainer.behaviors.Guarding;
import org.picocontainer.behaviors.Storing;
import org.picocontainer.web.PicoServletContainerListener;
import org.picocontainer.web.ScopedContainers;
import org.picocontainer.web.ThreadLocalLifecycleState;

@SuppressWarnings("serial")
public class WaffleListener extends PicoServletContainerListener {

    protected ScopedContainers makeScopedContainers() {
        DefaultPicoContainer application = new DefaultPicoContainer(new Guarding().wrap(new Caching()), new WaffleLifecycleStrategy(makeAppComponentMonitor()), makeParentContainer(), makeAppComponentMonitor());
        Storing sessionStoring = new Storing();
        DefaultPicoContainer session = new DefaultPicoContainer(new Guarding().wrap(sessionStoring), new WaffleLifecycleStrategy(makeSessionComponentMonitor()), application, makeSessionComponentMonitor());
        Storing requestStoring = new Storing();
        DefaultPicoContainer request = new DefaultPicoContainer(new Guarding().wrap(addRequestBehaviors(requestStoring)), new WaffleLifecycleStrategy(makeRequestComponentMonitor()), session, makeRequestComponentMonitor());
        ThreadLocalLifecycleState sessionState = new ThreadLocalLifecycleState();
        ThreadLocalLifecycleState requestState = new ThreadLocalLifecycleState();
        session.setLifecycleState(sessionState);
        request.setLifecycleState(requestState);
        return new ScopedContainers(application, session, request, sessionStoring, requestStoring, sessionState, requestState);
    }

}
