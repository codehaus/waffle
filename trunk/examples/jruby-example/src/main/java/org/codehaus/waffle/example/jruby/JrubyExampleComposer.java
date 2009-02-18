/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.example.jruby;

import org.codehaus.waffle.example.jruby.dao.SimplePersonDAO;
import org.codehaus.waffle.ruby.registrar.pico.RubyScriptComponentAdapter;
import org.codehaus.waffle.ruby.RubyWaffleComposer;
import org.codehaus.waffle.monitor.ActionMonitor;
import org.codehaus.waffle.monitor.CommonsLoggingMonitor;
import org.codehaus.waffle.monitor.BindMonitor;
import org.picocontainer.MutablePicoContainer;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

public class JrubyExampleComposer extends RubyWaffleComposer {

    @SuppressWarnings({"unchecked"})
    @Override
    public void composeApplication(MutablePicoContainer picoContainer, ServletContext servletContext) {
        super.composeApplication(picoContainer, servletContext);

        picoContainer.addComponent("the_dao", SimplePersonDAO.class);
        picoContainer.addComponent("chicago", "bears");

        List myList = new ArrayList();
        myList.add(15);
        picoContainer.addComponent(List.class, myList);

        picoContainer.addAdapter(new RubyScriptComponentAdapter("foobar", "FooBar")); // register the controller!
        picoContainer.addAdapter(new RubyScriptComponentAdapter("hello", "HelloController")); // register the controller!
        picoContainer.addAdapter(new RubyScriptComponentAdapter("person", "PersonController")); // register the controller!
    }

    protected Class<? extends ActionMonitor> actionMonitor() {
        return CommonsLoggingMonitor.class;
    }

    protected Class<? extends BindMonitor> bindMonitor() {
        return CommonsLoggingMonitor.class;
    }




    @Override
    public void composeRequest(MutablePicoContainer picoContainer) {
        super.composeRequest(picoContainer);
        picoContainer.addComponent(RubyScriptReloader.class);
    }

}
