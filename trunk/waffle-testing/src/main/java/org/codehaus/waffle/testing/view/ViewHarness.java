package org.codehaus.waffle.testing.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import org.codehaus.waffle.testing.view.freemarker.FreemarkerProcessor;
import org.codehaus.waffle.testing.view.sitemesh.SitemeshDecorator;

/**
 * Facade that enable processing of views by different template engines. The choice of engine is made based on the
 * extension of the template resource, eg .ftl will trigger use of Freemarker processor.
 * 
 * @author Mauro Talevi
 */
public class ViewHarness {

    enum Type {
        FREEMARKER
    };

    private final Properties configuration;

    public ViewHarness() {
        this(new Properties());
    }

    public ViewHarness(Properties configuration) {
        this.configuration = configuration;
    }

    public String process(String resource, Object controller) {
        return process(typeFor(resource), resource, controller);
    }

    public String process(Type type, String resource, Object controller) {
        return processorFor(type).process(resource, controller);
    }

    public ViewProcessor processorFor(String resource) {
        return processorFor(typeFor(resource));
    }

    public ViewProcessor processorFor(Type type) {
        switch (type) {
            case FREEMARKER:
                if (configuration.isEmpty()) {
                    return new FreemarkerProcessor();
                }
                return new FreemarkerProcessor(configuration);
            default:
                throw new UnknownTemplateTypeException(type.name());
        }
    }

    public Type typeFor(String resource) {
        if (resource.endsWith(".ftl")) {
            return Type.FREEMARKER;
        }
        throw new UnknownTemplateTypeException(resource);
    }

    @SuppressWarnings("serial")
    private static final class UnknownTemplateTypeException extends RuntimeException {

        public UnknownTemplateTypeException(String message) {
            super(message);
        }

    }

    /**
     * Processes a view with default configuration
     * 
     * @param resource the template resource path
     * @param controller the controller instance
     * @param debug the debug boolean flag
     * @return The processed resource
     */
    public static String processView(String resource, Object controller, boolean debug) {
        return processView(resource, new Properties(), controller, debug);
    }

    /**
     * Processes a view with custom configuration
     * 
     * @param resource the template resource path
     * @param configuration the view processor configuration
     * @param controller the controller instance
     * @param debug the debug boolean flag
     * @return The processed resource
     */
    public static String processView(String resource, Properties configuration, Object controller, boolean debug) {
        String processed = new ViewHarness(configuration).process(resource, controller);
        if (debug) {
            System.out.println(processed);
        }
        return processed;
    }

    /**
     * Decorates a view with Sitemesh and default processor configuration
     * 
     * @param resource the template resource path
     * @param controller the controller instance
     * @param decoratorsResource the Sitemesh decorators resource
     * @param decoratorName the decorator name
     * @param decoratorDataModel the decorator data model that can be used to override the processor data model
     * @return The decorated resource
     */
    public static String decorateView(String resource, Object controller, String decoratorsResource,
            String decoratorName, Map<String, Object> decoratorDataModel) {
        return decorateView(resource, new Properties(), Thread.currentThread().getContextClassLoader(), controller,
                decoratorsResource, decoratorName, decoratorDataModel);
    }

    /**
     * Decorates a view with Sitemesh and custom processor configuration
     * 
     * @param resource the template resource path
     * @param configuration the view processor configuration
     * @param classLaoder the ClassLoader used to load the decorator
     * @param controller the controller instance
     * @param decoratorsResource the Sitemesh decorators resource
     * @param decoratorName the decorator name
     * @param decoratorDataModel the decorator data model that can be used to override the processor data model
     * @return The decorated resource
     */
    public static String decorateView(String resource, Properties configuration, ClassLoader classLoader,
            Object controller, String decoratorsResource, String decoratorName, Map<String, Object> decoratorDataModel) {
        SitemeshDecorator decorator = new SitemeshDecorator(new ViewHarness(configuration).processorFor(resource),
                classLoader);
        return decorator.decorate(resource, controller, decoratorsResource, decoratorName, decoratorDataModel);
    }

    /**
     * Exports a view to a file
     * 
     * @param processed the String encoding the processed view
     * @param output the File to export view to
     * @throws IOException
     */
    public static void exportView(String processed, File output) throws IOException {
        File parent = new File(output.getParent());
        parent.mkdirs();
        exportView(processed, new FileOutputStream(output));
    }

    /**
     * Exports a view to an output stream
     * 
     * @param processed the String encoding the processed view
     * @param output the OutputStream to export view to
     * @throws IOException
     */
    public static void exportView(String processed, OutputStream output) throws IOException {
        output.write(processed.getBytes());
    }
}
