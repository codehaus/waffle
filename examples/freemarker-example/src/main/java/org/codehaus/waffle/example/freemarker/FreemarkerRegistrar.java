package org.codehaus.waffle.example.freemarker;

import static org.codehaus.waffle.bind.converters.DateValueConverter.DAY_FORMAT_KEY;
import static org.codehaus.waffle.bind.converters.DateValueConverter.TIME_FORMAT_KEY;

import java.util.Properties;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.bind.ValueConverterFinder;
import org.codehaus.waffle.bind.converters.AbstractValueConverter;
import org.codehaus.waffle.bind.converters.DateValueConverter;
import org.codehaus.waffle.example.freemarker.controller.DateProvider;
import org.codehaus.waffle.example.freemarker.controller.PersonController;
import org.codehaus.waffle.example.freemarker.converters.PersonListValueConverter;
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
        AbstractValueConverter converter = (AbstractValueConverter) registry.locateByType(DateValueConverter.class);
        if (converter != null) {
            Properties patterns = new Properties();
            patterns.setProperty(DAY_FORMAT_KEY, dateProvider.getDayPattern());
            patterns.setProperty(TIME_FORMAT_KEY, dateProvider.getTimePattern());
            converter.changePatterns(patterns);
        }
        registerInstance(dateProvider);
        register(SimplePersonPersister.class);
        register(PersonListValueConverter.class);
        ValueConverterFinder finder = registry.locateByType(ValueConverterFinder.class);
        finder.registerConverter((ValueConverter) getRegistered(PersonListValueConverter.class));
        register("people/manage", PersonController.class);
    }

}
