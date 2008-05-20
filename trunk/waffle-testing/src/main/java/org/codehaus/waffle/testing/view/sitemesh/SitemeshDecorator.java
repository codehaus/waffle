package org.codehaus.waffle.testing.view.sitemesh;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.codehaus.waffle.testing.view.ViewProcessor;

import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.PageParser;
import com.opensymphony.module.sitemesh.mapper.ConfigLoader;
import com.opensymphony.module.sitemesh.parser.HTMLPageParser;

/**
 * Sitemesh template decorator
 * 
 * @author Mauro Talevi
 */
public class SitemeshDecorator {

    private final ViewProcessor processor;

    public SitemeshDecorator(ViewProcessor processor) {
        this.processor = processor;
    }

    /**
     * Decorates a view with Sitemesh
     * 
     * @param resource the template resource path
     * @param controller the controller instance
     * @param decoratorsResource the Sitemesh decorators resource
     * @param decoratorName the decorator name
     * @param decoratorDataModel the decorator data model that can be used to override the processor data model
     * @return The decorated resource
     */
    public String decorate(String resource, Object controller, String decoratorsResource, String decoratorName,
            Map<String, Object> decoratorDataModel) {
        try {
            ConfigLoader configLoader = new ConfigLoader(createTempFile(decoratorsResource, "decorators-test", ".xml"));
            Decorator decorator = configLoader.getDecoratorByName(decoratorName);
            PageParser parser = new HTMLPageParser();
            Map<String, Object> dataModel = processor.createDataModel(controller);
            // Add/override any entries from the decorator data model
            for (String key : decoratorDataModel.keySet()) {
                dataModel.put(key, decoratorDataModel.get(key));
            }
            Page page = parser.parse(processor.process(resource, dataModel).toCharArray());
            // Add the page title and body 
            dataModel.put("title", page.getTitle());
            dataModel.put("body", page.getBody());
            return processor.process(decorator.getPage(), dataModel);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decorate resource", e);
        }
    }

    private File createTempFile(String resource, String prefix, String suffix) throws IOException {
        String content = loadContent(resource);
        File file = File.createTempFile(prefix, suffix);
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(content);
        out.close();
        return file;
    }

    private String loadContent(String resource) throws IOException {
        return IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream(resource));
    }
}
