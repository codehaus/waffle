package org.codehaus.waffle.example.freemarker;

import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.codehaus.waffle.bind.converters.DateValueConverter;
import org.codehaus.waffle.bind.converters.NumberListValueConverter;
import org.codehaus.waffle.bind.converters.NumberValueConverter;
import org.codehaus.waffle.bind.converters.StringListMapValueConverter;
import org.codehaus.waffle.bind.converters.StringListValueConverter;
import org.codehaus.waffle.bind.converters.StringNumberListMapValueConverter;
import org.codehaus.waffle.context.WaffleWebappComposer;
import org.codehaus.waffle.example.freemarker.controller.DateProvider;
import org.codehaus.waffle.example.freemarker.controller.PersonController;
import org.codehaus.waffle.example.freemarker.converters.PersonListValueConverter;
import org.codehaus.waffle.example.freemarker.converters.PersonValueConverter;
import org.codehaus.waffle.example.freemarker.persister.SimplePersonPersister;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.i18n.DefaultMessageResourcesConfiguration;
import org.codehaus.waffle.i18n.MessageResourcesConfiguration;
import org.codehaus.waffle.menu.Menu;
import org.codehaus.waffle.menu.MenuAwareController;
import org.codehaus.waffle.view.ViewResolver;
import org.codehaus.waffle.view.DefaultViewResolver;
import org.picocontainer.MutablePicoContainer;

public class FreemarkerExampleWebappComposer extends WaffleWebappComposer {
    private static final String DAY_PATTERN = "dd/MM/yyyy";
    private static final String TIME_PATTERN = "hh:mm:ss";

    @Override
    public void composeApplication(MutablePicoContainer picoContainer, ServletContext servletContext) {

        picoContainer.addComponent(MessageResourcesConfiguration.class, new DefaultMessageResourcesConfiguration("waffle-core-bundle,FreemarkerResources"));

        picoContainer.addComponent(NumberValueConverter.class);
        picoContainer.addComponent(StringListValueConverter.class);
        picoContainer.addComponent(NumberListValueConverter.class);
        picoContainer.addComponent(StringListMapValueConverter.class);
        picoContainer.addComponent(StringNumberListMapValueConverter.class);
        picoContainer.addComponent(PersonValueConverter.class);
        picoContainer.addComponent(PersonListValueConverter.class);
        picoContainer.addComponent(DateValueConverter.class, MyDateValueConverter.class);

        picoContainer.addComponent(SimplePersonPersister.class);

        super.composeApplication(picoContainer, servletContext);

        picoContainer.addComponent(new DateProvider(DAY_PATTERN, TIME_PATTERN, DAY_PATTERN));
        picoContainer.addComponent("people/manage", PersonController.class);
        picoContainer.addComponent("home", MenuAwareController.class);
        picoContainer.addComponent(createMenu());

    }

    public static class MyDateValueConverter extends DateValueConverter {
        public MyDateValueConverter(MessageResources messageResources) {
            super(messageResources, makeProperties());
        }
        private static Properties makeProperties() {
            Properties patterns = new Properties();
            patterns.setProperty(DAY_FORMAT_KEY, DAY_PATTERN);
            patterns.setProperty(TIME_FORMAT_KEY, TIME_PATTERN);
            return patterns;
        }
    }

    @Override
    protected Class<? extends ViewResolver> viewResolver() {
        return MyViewResolver.class;
    }
    public static class MyViewResolver extends DefaultViewResolver {
        public MyViewResolver() {
            configureView("home", "ftl/home");
        }
    }

    private Menu createMenu() {
        Map<String, List<String>> content = new HashMap<String, List<String>>();
        content.put("Home", asList("Home:home"));
        content.put("People", asList("Manage:people/manage"));
        return new Menu(content);
    }
}
