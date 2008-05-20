package org.codehaus.waffle.testing.view.freemarker;

import static org.codehaus.waffle.Constants.CONTROLLER_KEY;
import static org.codehaus.waffle.Constants.ERRORS_KEY;
import static org.codehaus.waffle.Constants.MESSAGES_KEY;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.codehaus.waffle.i18n.DefaultMessagesContext;
import org.codehaus.waffle.testing.view.ViewProcessor;
import org.codehaus.waffle.validation.DefaultErrorsContext;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;

/**
 * Freemarker template processor
 * 
 * @author Mauro Talevi
 */
public class FreemarkerProcessor implements ViewProcessor {

    public Configuration configuration;

    public FreemarkerProcessor() {
        this(defaultConfigurationProperties());
    }

    public FreemarkerProcessor(Properties properties) {
        this.configuration = configure(properties);
    }

    /**
     * Creates the default configuration properties, with
     * 
     * <pre>
     * properties.setProperty(&quot;templateLoadingPrefix&quot;, &quot;/&quot;);
     * </pre>
     * 
     * @return The configuration Properties
     */
    private static Properties defaultConfigurationProperties() {
        Properties properties = new Properties();
        properties.setProperty("templateLoadingPrefix", "/");
        return properties;
    }

    /**
     * Creates a configuration which retrieve resources from classpath
     * 
     * @param properties the initialisation Properties
     * @return The Configuration
     */
    private Configuration configure(Properties properties) {
        if (properties == null || !properties.containsKey("templateLoadingPrefix")) {
            throw new RuntimeException(
                    "Freemarker configuration properties requires property 'templateLoadingPrefix': " + properties,
                    null);
        }
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(FreemarkerProcessor.class, properties
                .getProperty("templateLoadingPrefix"));
        configuration.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
        return configuration;
    }

    public String process(String resource, Object controller) {
        return process(resource, createDataModel(controller));
    }

    /**
     * Processes template content with given data model
     * 
     * @param resource the template resource
     * @param dataModel the Map<String, Object> holding the data model
     * @return The processed content
     */
    public String process(String resource, Map<String, Object> dataModel) {
        try {
            Template template = configuration.getTemplate(resource);
            StringWriter writer = new StringWriter();
            template.process(dataModel, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to process Freemaker template " + resource + " with data model  "
                    + dataModel, e);
        }
    }

    /**
     * Creates an data model for the given controller.
     * The data model contains:
     * <ul>
     *   <li>"base": ""</li>
     *   <li>"controller": controller instance</li>
     *   <li>"errors": the default errors context</li>
     *   <li>"messages": the default messages context</li>
     * </ul>
     * 
     * @param controller the controller instance
     * @return A Map<String, Object>
     */
    public Map<String, Object> createDataModel(Object controller) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("base", "");
        model.put(CONTROLLER_KEY, controller);
        model.put(ERRORS_KEY, new DefaultErrorsContext(null));
        model.put(MESSAGES_KEY, new DefaultMessagesContext(null));
        return model;
    }

}
