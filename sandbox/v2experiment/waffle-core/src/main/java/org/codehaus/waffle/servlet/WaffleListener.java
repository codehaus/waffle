package org.codehaus.waffle.servlet;

import org.picocontainer.web.PicoServletContainerListener;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.LifecycleStrategy;
import org.picocontainer.monitors.NullComponentMonitor;
import org.picocontainer.behaviors.Guarding;
import org.picocontainer.behaviors.Caching;
import org.picocontainer.behaviors.Storing;
import org.codehaus.waffle.context.pico.WaffleLifecycleStrategy;

public class WaffleListener extends PicoServletContainerListener {

    protected ScopedContainers makeScopedContainers() {
        DefaultPicoContainer appCtnr = new DefaultPicoContainer(new Guarding().wrap(new Caching()), makeLifecycleStrategy(), makeParentContainer());
        Storing sessStoring = new Storing();
        DefaultPicoContainer sessCtnr = new DefaultPicoContainer(new Guarding().wrap(sessStoring), makeLifecycleStrategy(), appCtnr);
        Storing reqStoring = new Storing();
        DefaultPicoContainer reqCtnr = new DefaultPicoContainer(new Guarding().wrap(addRequestBehaviors(reqStoring)), makeLifecycleStrategy(), sessCtnr);
        return new ScopedContainers(appCtnr, sessCtnr, reqCtnr, sessStoring, reqStoring);
    }

    protected LifecycleStrategy makeLifecycleStrategy() {
        return new WaffleLifecycleStrategy(makeComponentMonitor());
    }


}
