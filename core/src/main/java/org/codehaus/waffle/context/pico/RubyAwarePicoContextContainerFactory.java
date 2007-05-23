package org.codehaus.waffle.context.pico;

import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.context.ContextContainer;
import org.picocontainer.MutablePicoContainer;
import org.jruby.Ruby;

public class RubyAwarePicoContextContainerFactory extends PicoContextContainerFactory {

    public RubyAwarePicoContextContainerFactory(MessageResources messageResources) {
        super(messageResources);
    }

    public ContextContainer buildApplicationContextContainer() {
        ContextContainer contextContainer = super.buildApplicationContextContainer();

        // Register RubyRuntime at Application level
        MutablePicoContainer picoContainer = (MutablePicoContainer) contextContainer.getDelegate();
        picoContainer.registerComponentInstance(Ruby.class, Ruby.getDefaultInstance());

        return contextContainer;
    }
    
}
