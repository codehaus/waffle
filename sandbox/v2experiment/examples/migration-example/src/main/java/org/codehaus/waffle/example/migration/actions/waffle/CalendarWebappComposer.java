package org.codehaus.waffle.example.migration.actions.waffle;

import org.codehaus.waffle.context.pico.WaffleWebappComposer;
import org.picocontainer.MutablePicoContainer;

public class CalendarWebappComposer extends WaffleWebappComposer {

    @Override
    public void composeSession(MutablePicoContainer picoContainer) {
        super.composeSession(picoContainer);
        picoContainer.addComponent("calendar", CalendarAction.class);
    }

}
