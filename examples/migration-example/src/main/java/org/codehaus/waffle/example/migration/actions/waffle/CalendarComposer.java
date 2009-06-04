package org.codehaus.waffle.example.migration.actions.waffle;

import org.codehaus.waffle.pico.WaffleComposer;
import org.picocontainer.MutablePicoContainer;

public class CalendarComposer extends WaffleComposer {

    @Override
    public void composeSession(MutablePicoContainer picoContainer) {
        super.composeSession(picoContainer);
        picoContainer.addComponent("calendar", CalendarAction.class);
    }

}
