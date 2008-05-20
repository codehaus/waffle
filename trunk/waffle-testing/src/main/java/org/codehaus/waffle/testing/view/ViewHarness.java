package org.codehaus.waffle.testing.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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

    public String process(String resource, Object controller) {
        return process(typeFor(resource), resource, controller);
    }

    public String process(Type type, String resource, Object controller) {
        return processorFor(type).process(resource, controller);
    }

    public ViewProcessor processorFor(Type type) {
        switch (type) {
            case FREEMARKER:
                return new FreemarkerProcessor();
            default:
                throw new UnknownTemplateTypeException(type.name());
        }
    }

    public ViewProcessor processorFor(String resource) {
        return processorFor(typeFor(resource));
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
     * Processes a view
     * 
     * @param resource the template resource path
     * @param controller the controller instance
     * @param debug the debug boolean flag
     * @return The processed resource
     */
    public static String processView(String resource, Object controller, boolean debug) {
        String processed = new ViewHarness().process(resource, controller);
        if (debug) {
            System.out.println(processed);
        }
        return processed;
    }

    /**
     * Decorates a view with Sitemesh
     * 
     * @param resource the template resource path
     * @param controller the controller instance
     * @param decoratorsResource the Sitemesh decorators resource
     * @param decoratorName the decorator name
     * @return The decorated resource
     */
    public static String decorateView(String resource, Object controller, String decoratorsResource,
            String decoratorName) {
        SitemeshDecorator decorator = new SitemeshDecorator(new ViewHarness().processorFor(resource));
        return decorator.decorate(resource, controller, decoratorsResource, decoratorName);
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
