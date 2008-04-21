package org.codehaus.waffle.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * 
 * @author Mauro Talevi
 */
public class ExportViewTest {

    @Test
    public void canCreateExportView() {
        String content = "1,2,3";
        String contentType = "text/csv";
        String filename = "export.csv";
        Object controller = new Object();
        ExportView view = new ExportView(controller, contentType, content.getBytes(), filename);
        assertEquals(content, new String(view.getContent()));
        assertEquals(contentType, view.getContentType());
        assertEquals(filename, view.getFilename());
        assertEquals(controller, view.getController());
        assertNull(view.getPath());
    }

}
