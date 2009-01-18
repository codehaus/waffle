package org.codehaus.waffle.example.freemarker;

import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.bind.ValueConverterFinder;
import org.codehaus.waffle.bind.converters.AbstractValueConverter;
import org.codehaus.waffle.bind.converters.DateValueConverter;
import org.codehaus.waffle.bind.converters.NumberValueConverter;
import org.codehaus.waffle.bind.converters.StringListValueConverter;
import org.codehaus.waffle.bind.converters.NumberListValueConverter;
import org.codehaus.waffle.bind.converters.StringListMapValueConverter;
import org.codehaus.waffle.bind.converters.StringNumberListMapValueConverter;
import static org.codehaus.waffle.bind.converters.DateValueConverter.DAY_FORMAT_KEY;
import static org.codehaus.waffle.bind.converters.DateValueConverter.TIME_FORMAT_KEY;

import org.codehaus.waffle.context.pico.WaffleWebappComposer;
import org.codehaus.waffle.example.freemarker.controller.DateProvider;
import org.codehaus.waffle.example.freemarker.controller.PersonController;
import org.codehaus.waffle.example.freemarker.converters.PersonListValueConverter;
import org.codehaus.waffle.example.freemarker.converters.PersonValueConverter;
import org.codehaus.waffle.example.freemarker.persister.SimplePersonPersister;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.menu.Menu;
import org.codehaus.waffle.menu.MenuAwareController;
import org.codehaus.waffle.view.ViewResolver;
import org.picocontainer.MutablePicoContainer;

import javax.servlet.ServletContext;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class FreemarkerExampleWebappComposer extends WaffleWebappComposer {

    @Override
    public void composeApplication(MutablePicoContainer picoContainer, ServletContext servletContext) {
        super.composeApplication(picoContainer, servletContext);
        picoContainer.addComponent("DateValueConverter", DateValueConverter.class);
        DateProvider dateProvider = new DateProvider("dd/MM/yyyy", "hh:mm:ss", "dd/MM/yyyy");
        AbstractValueConverter converter = (AbstractValueConverter) picoContainer.getComponent(DateValueConverter.class);
        if (converter != null) {
            Properties patterns = new Properties();
            patterns.setProperty(DAY_FORMAT_KEY, dateProvider.getDayPattern());
            patterns.setProperty(TIME_FORMAT_KEY, dateProvider.getTimePattern());
            converter.changePatterns(patterns);
        }
        picoContainer.addComponent("NumberValueConverter", NumberValueConverter.class);
        picoContainer.addComponent("StringListValueConverter", StringListValueConverter.class);
        picoContainer.addComponent("NumberListValueConverter", NumberListValueConverter.class);
        picoContainer.addComponent("StringListMapValueConverter", StringListMapValueConverter.class);
        picoContainer.addComponent("StringNumberListMapValueConverter", StringNumberListMapValueConverter.class);
        picoContainer.addComponent(dateProvider);
        picoContainer.addComponent(SimplePersonPersister.class);
        picoContainer.addComponent(PersonValueConverter.class);
        picoContainer.addComponent(PersonListValueConverter.class);
        ValueConverterFinder finder = picoContainer.getComponent(ValueConverterFinder.class);
        finder.registerConverter((ValueConverter) picoContainer.getComponent(PersonValueConverter.class));
        finder.registerConverter((ValueConverter) picoContainer.getComponent(PersonListValueConverter.class));
        MessageResources resources = picoContainer.getComponent(MessageResources.class);
        resources.useURI("waffle-core-bundle,FreemarkerResources");
        resources.useLocale(Locale.ITALIAN);
        picoContainer.addComponent("people/manage", PersonController.class);
        picoContainer.addComponent("home", MenuAwareController.class);
        ViewResolver viewResolver = picoContainer.getComponent(ViewResolver.class);
        viewResolver.configureView("home", "ftl/home");
        picoContainer.addComponent(createMenu());

    }

    private Menu createMenu() {
        Map<String, List<String>> content = new HashMap<String, List<String>>();
        content.put("Home", asList("Home:home"));
        content.put("People", asList("Manage:people/manage"));
        return new Menu(content);
    }
}
