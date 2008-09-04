package org.codehaus.waffle.example.freemarker;

import static org.codehaus.waffle.bind.converters.DateValueConverter.DAY_FORMAT_KEY;
import static org.codehaus.waffle.bind.converters.DateValueConverter.TIME_FORMAT_KEY;

import java.util.Properties;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.bind.converters.DateValueConverter;
import org.codehaus.waffle.example.freemarker.controller.DateProvider;
import org.codehaus.waffle.example.freemarker.controller.PersonController;
import org.codehaus.waffle.example.freemarker.persister.SimplePersonPersister;
import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.Registrar;

public class FreemarkerRegistrar extends AbstractRegistrar {

    public FreemarkerRegistrar(Registrar delegate) {
        super(delegate);
    }

    @Override
    public void application() {
        ComponentRegistry registry = getComponentRegistry();
        DateProvider dateProvider = new DateProvider("dd/MM/yyyy", "hh:mm:ss", "dd/MM/yyyy");
        DateValueConverter converter = (DateValueConverter) registry.locateByType(DateValueConverter.class);
        if (converter != null) {
            Properties patterns = new Properties();
            patterns.setProperty(DAY_FORMAT_KEY, dateProvider.getDayPattern());
            patterns.setProperty(TIME_FORMAT_KEY, dateProvider.getTimePattern());
            converter.changePatterns(patterns);
        }
        registerInstance(dateProvider);
        register(SimplePersonPersister.class);
    }

    @Override
    public void session() {
        register("people/manage", PersonController.class);
    }
}