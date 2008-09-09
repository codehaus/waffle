package org.codehaus.waffle.example.freemarker;

import static java.util.Arrays.asList;
import static org.codehaus.waffle.bind.converters.DateValueConverter.DAY_FORMAT_KEY;
import static org.codehaus.waffle.bind.converters.DateValueConverter.TIME_FORMAT_KEY;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.bind.ValueConverterFinder;
import org.codehaus.waffle.bind.converters.AbstractValueConverter;
import org.codehaus.waffle.bind.converters.DateValueConverter;
import org.codehaus.waffle.example.freemarker.controller.DateProvider;
import org.codehaus.waffle.example.freemarker.controller.PersonController;
import org.codehaus.waffle.example.freemarker.converters.PersonListValueConverter;
import org.codehaus.waffle.example.freemarker.converters.PersonValueConverter;
import org.codehaus.waffle.example.freemarker.persister.SimplePersonPersister;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.menu.Menu;
import org.codehaus.waffle.menu.MenuAwareController;
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
        register(PersonValueConverter.class);
        register(PersonListValueConverter.class);
        ValueConverterFinder finder = registry.locateByType(ValueConverterFinder.class);
        finder.registerConverter((ValueConverter) getRegistered(PersonValueConverter.class));
        finder.registerConverter((ValueConverter) getRegistered(PersonListValueConverter.class));
        MessageResources resources = registry.locateByType(MessageResources.class);
        resources.useBundleName("FreemarkerResources");
        resources.useLocale(Locale.ITALIAN);
        register("people/manage", PersonController.class);
        register("home", MenuAwareController.class);
        registerInstance(createMenu());
    }

    private Menu createMenu() {
        Map<String, List<String>> content = new HashMap<String, List<String>>();
        content.put("Home", asList("Home:home"));
        content.put("People", asList("Manage:people/manage"));
        return new Menu(content);
    }
}
