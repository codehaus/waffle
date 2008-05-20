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
import com.opensymphony.module.sitemesh.multipass.DivExtractingPageParser;

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

    public String decorate(String resource, Object controller, String decoratorsResource, String decoratorName) {
        try {
            ConfigLoader configLoader = new ConfigLoader(createTempFile(decoratorsResource, "decorators-test", ".xml"));
            Decorator decorator = configLoader.getDecoratorByName(decoratorName);
            PageParser parser = new DivExtractingPageParser();            
            Page page = parser.parse(processor.process(resource, controller).toCharArray());
            Map<String, Object> dataModel = processor.createDataModel(controller);
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
