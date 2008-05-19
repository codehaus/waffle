package org.codehaus.waffle.testing.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.codehaus.waffle.testing.view.freemarker.FreemarkerProcessor;

/**
 * Facade that enable processing of views by different template engines. The choice of engine is made based on the
 * extension of the template resource, eg .ftl will trigger use of Freemarker processor.
 * 
 * @author Mauro Talevi
 */
public class ViewHarness {

    enum Template {
        FREEMARKER
    };

    public String process(String resource, Object controller) {
        Template template = templateFor(resource);
        switch (template) {
            case FREEMARKER:
                FreemarkerProcessor processor = new FreemarkerProcessor();
                return processor.process(resource, controller);
            default:
                throw new UnknownTemplateTypeException(resource);
        }
    }

    private Template templateFor(String resource) {
        if (resource.endsWith(".ftl")) {
            return Template.FREEMARKER;
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
     * Exports a view to a file
     * 
     * @param resource the template resource path
     * @param controller the controller instance
     * @param output the File to export view to
     * @throws IOException
     */
    public static void exportView(String resource, Object controller, File output) throws IOException {
        File parent = new File(output.getParent());
        parent.mkdirs();
        exportView(resource, controller, new FileOutputStream(output));
    }

    /**
     * Exports a view to an output stream
     * 
     * @param resource the template resource path
     * @param controller the controller instance
     * @param output the OutputStream to export view to
     * @throws IOException
     */
    public static void exportView(String resource, Object controller, OutputStream output) throws IOException {
        String processed = new ViewHarness().process(resource, controller);
        output.write(processed.getBytes());
    }
}
