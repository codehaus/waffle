package org.codehaus.waffle.testing.view;

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
     * Static entry point to ViewHarness
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
}
